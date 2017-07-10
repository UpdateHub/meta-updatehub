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

def uhupkg_unpack(required, d):
    def uhupkg_exists():
        for u in fetcher.urls:
            ud = fetcher.ud[u]

            if ud and isinstance(ud.method, bb.fetch2.local.Local):
                paths = ud.method.localpaths(ud, d)
                for f in paths:
                    pth = ud.decodedurl
                    if '*' in pth:
                        f = os.path.join(os.path.abspath(f), pth)
                    if os.path.exists(f):
                        return True
        return False

    # Remove any leftover from previous run
    uhupkg = d.getVar('UHUPKG', True)
    if os.path.exists(uhupkg):
        os.unlink(uhupkg)
        bb.debug(1, "Removed a leftover uhupkg.config from previous run")

    uhupkg_uri = [ "file://uhupkg.config" ]

    fetcher = bb.fetch2.Fetch(uhupkg_uri, d, cache = False, localonly = True)

    # Make existing uhupkg.config available for use
    if uhupkg_exists() or required:
        try:
            fetcher = bb.fetch2.Fetch(uhupkg_uri, d)
            fetcher.unpack(d.getVar('DEPLOY_DIR_IMAGE', True))
        except bb.fetch2.BBFetchException as e:
            bb.fatal(str(e))
    else:
        return False

    return True


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

UHUPKG = "${DEPLOY_DIR_IMAGE}/uhupkg.config"
UPDATEHUB_SERVER_URL ?= "api.updatehub.io"

uhushell_prepare() {
    # Remove any leftover from previous run
    if [ -e .uhu ]; then
        rm -v .uhu
    fi

    if [ -n "${UPDATEHUB_SERVER_URL}" ]; then
        export UHU_SERVER_URL=${UPDATEHUB_SERVER_URL}
    fi
    if [ -e ${UHUPKG} ]; then
        mv ${UHUPKG} .uhu
    fi
    uhu hardware add "${MACHINE}"
    uhu product use "${UPDATEHUB_PRODUCT_UID}"
    uhu package version "${UPDATEHUB_PACKAGE_VERSION}"
}
uhushell_prepare[dirs] ?= "${DEPLOY_DIR_IMAGE}"

uhushell_finish() {
    uhu package export ${UHUPKG}
    uhu cleanup
}
uhushell_finish[dirs] ?= "${DEPLOY_DIR_IMAGE}"

python do_uhushell () {
    if not uhupkg_unpack(False, d):
        bb.warn("No existing uhupkg.config file has been found. A new one will be created for you.")

    bb.build.exec_func('uhushell_prepare', d)
    oe_terminal("${SHELL} -c 'uhu'", "UpdateHub Shell", d)
    bb.build.exec_func('uhushell_finish', d)

    uhupkg = d.getVar('UHUPKG', True)
    bb.plain("UpdateHub package exported in '%s'. Please add it to your image directory in 'files/uhupkg.config'." % uhupkg)
}

addtask uhushell after do_image_complete do_unpack
do_uhushell[dirs] ?= "${DEPLOY_DIR_IMAGE}"
do_uhushell[nostamp] = "1"

uhuarchive_run() {
    if [ -e ${UHUPKG} ]; then
        mv ${UHUPKG} .uhu
    else
        uhu product use ${UPDATEHUB_PRODUCT_UID}
    fi
    uhu package version "${UPDATEHUB_PACKAGE_VERSION}"
    uhu package archive --output ${IMAGE_NAME}.uhupkg
    uhu cleanup
    ln -sf ${IMAGE_NAME}.uhupkg ${IMAGE_LINK_NAME}.uhupkg
}
uhuarchive_run[dirs] ?= "${DEPLOY_DIR_IMAGE}"
uhuarchive_run[nostamp] = "1"

python do_uhuarchive () {
    uhupkg_unpack(True, d)
    bb.build.exec_func('uhuarchive_run', d)
}

addtask uhuarchive after do_image_complete do_unpack
do_uhuarchive[nostamp] = "1"

uhupush_run() {
    if [ -n "${UPDATEHUB_SERVER_URL}" ]; then
        export UHU_SERVER_URL=${UPDATEHUB_SERVER_URL}
    fi
    if [ -e ${UHUPKG} ]; then
        mv ${UHUPKG} .uhu
    else
        uhu product use ${UPDATEHUB_PRODUCT_UID}
    fi
    uhu package version "${UPDATEHUB_PACKAGE_VERSION}"
    uhu package push
    uhu cleanup
}
uhupush_run[dirs] ?= "${DEPLOY_DIR_IMAGE}"
uhupush_run[nostamp] = "1"

python do_uhupush () {
    uhupkg_unpack(True, d)
    bb.build.exec_func('uhupush_run', d)
}

addtask uhupush after do_image_complete do_unpack
do_uhupush[nostamp] = "1"
