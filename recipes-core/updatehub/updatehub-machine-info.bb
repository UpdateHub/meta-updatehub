SUMMARY = "UpdateHub - Machine Information"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

S = "${WORKDIR}"

do_install() {
    echo 'MACHINE="${MACHINE}"' > machine
    install -Dm 644 machine ${D}${sysconfdir}/updatehub/machine
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
