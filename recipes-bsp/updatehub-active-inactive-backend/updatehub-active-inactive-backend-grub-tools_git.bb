SUMMARY = "UpdateHub - Active/Inactive GRUB backend tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/UpdateHub/active-inactive-backend-grub.git;protocol=https"
SRCREV = "6bf20937d1a5d96176addaf11a64aefc37ebc392"

S = "${WORKDIR}/git"

PV = "0.0+${SRCPV}"

inherit allarch

do_configure[noexec] = "1"

do_install () {
    install -Dm 0755 updatehub-active-get ${D}${bindir}/updatehub-active-get
    install -Dm 0755 updatehub-active-set ${D}${bindir}/updatehub-active-set
}
