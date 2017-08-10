SUMMARY = "UpdateHub - Active/Inactive U-Boot backend"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/UpdateHub/active-inactive-backend-u-boot.git;protocol=https"
SRCREV = "4249f67a80fec383aaf28197aedf875c1c7167d2"

S = "${WORKDIR}/git"

PV = "0.0+${SRCPV}"

inherit allarch

do_configure[noexec] = "1"

do_install () {
    install -Dm 0755 updatehub-active-get ${D}${bindir}/updatehub-active-get
    install -Dm 0755 updatehub-active-set ${D}${bindir}/updatehub-active-set
}

RDEPENDS_${PN} += "u-boot-fw-utils"
INSANE_SKIP_${PN} += "build-deps"
