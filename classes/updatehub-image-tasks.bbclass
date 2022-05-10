# Handle updatehub image integration tasks
#
# This class is not intended to be used directly but through the
# updatehub-image class.
#
# For more information about its usage, is available in
# 'updatehub-image' class documentation.
#
# Copyright 2017-2020 (C) O.S. Systems Software LTDA.

inherit terminal python3native updatehub-runtime

UHUPKG_FILES ?= "${IMAGE_BASENAME}.${MACHINE}.uhupkg.config ${IMAGE_BASENAME}.uhupkg.config ${MACHINE}.uhupkg.config"
UHUPKG_SEARCH_PATH ?= "${THISDIR}:${@':'.join('%s/uhu' % p for p in '${BBPATH}'.split(':'))}"
UHUPKG_FULL_PATH = "${@uhupkg_search(d.getVar('UHUPKG_FILES', True).split(), d.getVar('UHUPKG_SEARCH_PATH', True)) or ''}"

def uhupkg_search(files, search_path):
    for f in files:
        if os.path.isabs(f):
            if os.path.exists(f):
                return f
        else:
            searched = bb.utils.which(search_path, f)
            if searched:
                return searched

python () {
    ### Queue initramfs image build
    image_type = d.getVar("UPDATEHUB_IMAGE_TYPE", False)
    if image_type in ['initramfs']:
        d.appendVarFlag('do_generate_updatehub_dependencies',
                        'depends',
                        'updatehub-initramfs-image:do_rootfs')
}

addtask generate_updatehub_dependencies before do_image_complete
do_generate_updatehub_dependencies() {
    :
}

UHUPKG = "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}.${MACHINE}.uhupkg.config"
UPDATEHUB_ACCESS_ID ?= ""
UPDATEHUB_ACCESS_SECRET ?= ""

uhu_setup() {
    need_key=$1

    # Remove any leftover from previous run
    if [ -e ".uhu" ] || [ -e "${UHUPKG}" ]; then
        rm -f ".uhu" "${UHUPKG}"
        bbdebug 1 "Removed a leftover uhu configuration files from previous run"
    fi

    if [ -n "${UHUPKG_FULL_PATH}" ]; then
        cp ${UHUPKG_FULL_PATH} .uhu
        bbdebug 1 "Copied ${UHUPKG_FULL_PATH} as .uhu"
    else
        bbwarn "No uhupkg.config files from UHUPKG_FILES were found: ${UHUPKG_FILES}. Please set UHUPKG_FILE or UHUPKG_FILES appropriately."
    fi

    if [ -n "${UPDATEHUB_SERVER_URL}" ]; then
        export UHU_SERVER_URL="${UPDATEHUB_SERVER_URL}"
    fi

    if [ -n "${UPDATEHUB_CUSTOM_CA_CERTS}" ]; then
        export UHU_CUSTOM_CA_CERTS="${UPDATEHUB_CUSTOM_CA_CERTS}"
    fi

    export UHU_PRIVATE_KEY="${UPDATEHUB_UHUPKG_PRIVATE_KEY}"

    uhu hardware reset

    if [ -n "${UPDATEHUB_COMPATIBLE_MACHINE}" ]; then
        machine_list='${UPDATEHUB_COMPATIBLE_MACHINE}'
        for machine in $machine_list; do
            uhu hardware add "$machine"
        done
    else
        bberror "UPDATEHUB_COMPATIBLE_MACHINE is not set. Aborting."
    fi

    uhu product use "${UPDATEHUB_PRODUCT_UID}"
    uhu package version "${UPDATEHUB_PACKAGE_VERSION}"

    # Replace few commonly used variables
    sed -e "s,\$IMAGE_BASENAME,${IMAGE_BASENAME},g" \
        -e "s,\$MACHINE,${MACHINE},g" \
        -e "s,\$UBOOT_BINARY,${UBOOT_BINARY},g" \
        -e "s,\$SPL_BINARY,${SPL_BINARY},g" \
        -e "s,\$SDIMG_KERNELIMAGE,${SDIMG_KERNELIMAGE},g" \
        -e "s,\$BOOTFILES_DIR_NAME,${BOOTFILES_DIR_NAME},g" \
        -i .uhu
}
uhu_setup[dirs] ?= "${DEPLOY_DIR_IMAGE}"

uhushell_finish() {
    uhu package export ${UHUPKG}
    uhu cleanup

    # Replace few commonly used variables
    sed -e "s,${IMAGE_BASENAME},\$IMAGE_BASENAME,g" \
        -e "s,${MACHINE},\$MACHINE,g" \
        -e "s,${UBOOT_BINARY},\$UBOOT_BINARY,g" \
        -e "s,\$SPL_BINARY,${SPL_BINARY},g" \
        -i ${UHUPKG}
}
uhushell_finish[dirs] ?= "${DEPLOY_DIR_IMAGE}"

python do_uhushell () {
    bb.build.exec_func('uhu_setup', d)

    # We need to know when the command completes we therefore write
    # the pid to a file using a wrapper script, then monitor the pid
    # until it exits.
    import tempfile
    pidfile = tempfile.NamedTemporaryFile(delete = False).name
    try:
        oe_terminal("${SHELL} -c 'echo $$ > %s ; UHU_ACCESS_ID=\"%s\" UHU_ACCESS_SECRET=\"%s\" UHU_PRIVATE_KEY=\"%s\" uhu'" % (
            pidfile,
            d.getVar('UPDATEHUB_ACCESS_ID'),
            d.getVar('UPDATEHUB_ACCESS_SECRET'),
            d.getVar('UPDATEHUB_UHUPKG_PRIVATE_KEY')
        ), "updatehub Shell", d)
        while os.stat(pidfile).st_size <= 0:
            continue
        with open(pidfile, "r") as f:
            pid = int(f.readline())
    finally:
        os.unlink(pidfile)

    import time
    while True:
        try:
            os.kill(pid, 0)
        except OSError:
            break
        time.sleep(0.1)

    bb.build.exec_func('uhushell_finish', d)

    uhupkg = d.getVar('UHUPKG', True)
    bb.plain("updatehub package exported in '%s'. Please add it to your image directory." % uhupkg)
}

addtask uhushell after do_image_complete do_unpack
do_uhushell[depends] += "uhu-native:do_populate_sysroot"
do_uhushell[dirs] ?= "${DEPLOY_DIR_IMAGE}"
do_uhushell[nostamp] = "1"

uhuarchive_run() {
    uhu_setup

    uhu package archive --output ${IMAGE_NAME}.uhupkg
    uhu cleanup
    ln -sf ${IMAGE_NAME}.uhupkg ${IMAGE_LINK_NAME}.uhupkg
}
uhuarchive_run[dirs] ?= "${DEPLOY_DIR_IMAGE}"
uhuarchive_run[nostamp] = "1"

python do_uhuarchive () {
    bb.build.exec_func('uhuarchive_run', d)
}

addtask uhuarchive after do_image_complete do_unpack
do_uhuarchive[depends] += "uhu-native:do_populate_sysroot"
do_uhuarchive[nostamp] = "1"
do_uhuarchive[recrdeptask] += "do_deploy"

uhupush_run() {
    uhu_setup

    if [ -n "${UPDATEHUB_ACCESS_ID}" ] && [ -z "${UPDATEHUB_ACCESS_SECRET}" ] || \
           [ -z "${UPDATEHUB_ACCESS_ID}" ] && [ -n "${UPDATEHUB_ACCESS_SECRET}" ]; then
        bberror "Both UPDATEHUB_ACCESS_ID and UPDATEHUB_ACCESS_SECRET must be set. Aborting."
    fi

    if [ -n "${UPDATEHUB_ACCESS_ID}" ] && [ -n "${UPDATEHUB_ACCESS_SECRET}" ]; then
        export UHU_ACCESS_ID="${UPDATEHUB_ACCESS_ID}"
        export UHU_ACCESS_SECRET="${UPDATEHUB_ACCESS_SECRET}"
    fi

    uhu package push

    uhu cleanup
}
uhupush_run[dirs] ?= "${DEPLOY_DIR_IMAGE}"
uhupush_run[nostamp] = "1"
uhupush_run[progress] = "percent"

python do_uhupush () {
    bb.build.exec_func('uhupush_run', d)
}

addtask uhupush after do_image_complete do_unpack
do_uhupush[depends] += "uhu-native:do_populate_sysroot"
do_uhupush[nostamp] = "1"
do_uhupush[recrdeptask] += "do_deploy"
do_uhupush[network] = "1"

addtask validate_updatehub_settings before do_uhupush
addtask validate_updatehub_settings before do_uhuarchive
do_validate_updatehub_settings[nostamp] = "1"
python do_validate_updatehub_settings() {
    ### Ensures product uid is set
    product_uid = d.getVar("UPDATEHUB_PRODUCT_UID", False)
    if product_uid == '0000000000000000000000000000000000000000000000000000000000000000':
       bb.warn("'UPDATEHUB_PRODUCT_UID' is set to a generic id")

    ### Ensure a valid public and private keys are provided
    uhupkg_public_key = d.getVar('UPDATEHUB_UHUPKG_PUBLIC_KEY', True)
    uhupkg_private_key = d.getVar('UPDATEHUB_UHUPKG_PRIVATE_KEY', True)
    if not uhupkg_public_key or not uhupkg_private_key:
       bb.warn("'UPDATEHUB_UHUPKG_PUBLIC_KEY' and 'UPDATEHUB_UHUPKG_PRIVATE_KEY' variables must be set to allow image authenticity verification. The image authenticity will not be verified in the system.")
}
