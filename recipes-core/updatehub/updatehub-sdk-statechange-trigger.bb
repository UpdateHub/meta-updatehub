SUMMARY = "updatehub SDK - statechange trigger"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
    file://10-updatehub-sdk-statechange-trigger \
"

S = "${WORKDIR}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0755 ${WORKDIR}/10-updatehub-sdk-statechange-trigger \
            ${D}${datadir}/updatehub/state-change-callbacks.d/10-updatehub-sdk-statechange-trigger
}

FILES_${PN} += "${datadir}/updatehub"
RDEPENDS_${PN} = "updatehub-callbacks netcat-openbsd"
