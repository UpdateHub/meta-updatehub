# Handle UpdateHub image integration tasks
#
# This class is not intended to be used directly but through the
# updatehub-image class.
#
# For more information about its usage, is available in
# 'updatehub-image' class documentation.
#
# Copyright 2017 (C) O.S. Systems Software LTDA.

inherit terminal python3native

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

DEPENDS += "uhu-native"

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
UPDATEHUB_SERVER_URL ?= "api.updatehub.io"

uhu_setup() {
    # Remove any leftover from previous run
    if [ -e "${UHUPKG}" ]; then
        rm "${UHUPKG}"
        bbdebug 1 "Removed a leftover uhupkg.config from previous run"
    fi

    if [ -z "${UHUPKG_FULL_PATH}" ]; then
        bbfatal "No uhupkg.config files from UHUPKG_FILES were found: ${UHUPKG_FILES}. Please set UHUPKG_FILE or UHUPKG_FILES appropriately."
    fi

    if [ -n "${UPDATEHUB_SERVER_URL}" ]; then
        export UHU_SERVER_URL=${UPDATEHUB_SERVER_URL}
    fi

    cp ${UHUPKG_FULL_PATH} .uhu
    bbdebug 1 "Copied ${UHUPKG_FULL_PATH} as .uhu"

    uhu hardware reset
    uhu hardware add "${MACHINE}"
    uhu product use "${UPDATEHUB_PRODUCT_UID}"
    uhu package version "${UPDATEHUB_PACKAGE_VERSION}"

    # Replace few commonly used variables
    sed -e "s,\$IMAGE_BASENAME,${IMAGE_BASENAME},g" \
        -e "s,\$MACHINE,${MACHINE},g" \
        -i .uhu
}
uhu_setup[dirs] ?= "${DEPLOY_DIR_IMAGE}"

uhushell_finish() {
    uhu package export ${UHUPKG}
    uhu cleanup

    # Replace few commonly used variables
    sed -e "s,${IMAGE_BASENAME},\$IMAGE_BASENAME,g" \
        -e "s,${MACHINE},\$MACHINE,g" \
        -i ${UHUPKG}
}
uhushell_finish[dirs] ?= "${DEPLOY_DIR_IMAGE}"

python do_uhushell () {
    bb.build.exec_func('uhu_setup', d)
    oe_terminal("${SHELL} -c 'uhu'", "UpdateHub Shell", d)
    bb.build.exec_func('uhushell_finish', d)

    uhupkg = d.getVar('UHUPKG', True)
    bb.plain("UpdateHub package exported in '%s'. Please add it to your image directory." % uhupkg)
}

addtask uhushell after do_image_complete do_unpack
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
do_uhuarchive[nostamp] = "1"

uhupush_run() {
    uhu_setup

    uhu package push
    uhu cleanup
}
uhupush_run[dirs] ?= "${DEPLOY_DIR_IMAGE}"
uhupush_run[nostamp] = "1"

python do_uhupush () {
    bb.build.exec_func('uhupush_run', d)
}

addtask uhupush after do_image_complete do_unpack
do_uhupush[nostamp] = "1"
