SUMMARY = "updatehub - Callbacks"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
    file://state-change-callback \
    file://error-callback \
    file://validate-callback \
    file://rollback-callback \
"

S = "${WORKDIR}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0755 ${WORKDIR}/state-change-callback ${D}${datadir}/updatehub/state-change-callback
    install -Dm 0755 ${WORKDIR}/error-callback ${D}${datadir}/updatehub/error-callback
    install -Dm 0755 ${WORKDIR}/validate-callback ${D}${datadir}/updatehub/validate-callback
    install -Dm 0755 ${WORKDIR}/rollback-callback ${D}${datadir}/updatehub/rollback-callback
    install -d ${D}${datadir}/updatehub/state-change-callbacks.d
    install -d ${D}${datadir}/updatehub/error-callbacks.d
    install -d ${D}${datadir}/updatehub/validate-callbacks.d
    install -d ${D}${datadir}/updatehub/rollback-callbacks.d
}

FILES_${PN} += "${datadir}/updatehub"
