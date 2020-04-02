SUMMARY = "updatehub - Active/Inactive U-Boot backend"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/updatehub/active-inactive-backend-u-boot.git;protocol=https"
SRCREV = "9a8daea29a7b82d2c43480619be2ec47803b83f7"

S = "${WORKDIR}/git"

PV = "0.0+${SRCPV}"

inherit allarch

do_configure[noexec] = "1"

do_install () {
    install -Dm 0755 updatehub-active-get ${D}${bindir}/updatehub-active-get
    install -Dm 0755 updatehub-active-set ${D}${bindir}/updatehub-active-set
    install -Dm 0755 updatehub-active-validated ${D}${bindir}/updatehub-active-validated
}

RDEPENDS_${PN} += "u-boot-fw-utils u-boot-default-env"
INSANE_SKIP_${PN} += "build-deps"
