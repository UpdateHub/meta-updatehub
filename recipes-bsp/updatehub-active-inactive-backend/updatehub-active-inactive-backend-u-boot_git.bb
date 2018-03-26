SUMMARY = "updatehub - Active/Inactive U-Boot backend"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/updatehub/active-inactive-backend-u-boot.git;protocol=https"
SRCREV = "e09ef86355dce35704615a0e1e7bec6080c89eb3"

S = "${WORKDIR}/git"

PV = "0.0+${SRCPV}"

inherit allarch

do_configure[noexec] = "1"

do_install () {
    install -Dm 0755 updatehub-active-get ${D}${bindir}/updatehub-active-get
    install -Dm 0755 updatehub-active-set ${D}${bindir}/updatehub-active-set
    install -Dm 0755 updatehub-active-validated ${D}${bindir}/updatehub-active-validated
}

RDEPENDS_${PN} += "u-boot-fw-utils"
INSANE_SKIP_${PN} += "build-deps"
