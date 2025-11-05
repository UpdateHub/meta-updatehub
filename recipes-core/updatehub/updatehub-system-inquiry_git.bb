SUMMARY = "updatehub - System Inquiry"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/updatehub/system-inquiry.git;protocol=https;branch=master"
SRCREV = "399c52ed503e700fdb266c049520a15312f84b3e"

PV = "0.0+${SRCPV}"

inherit allarch

do_install() {
    install -Dm755 product-uid ${D}${datadir}/updatehub/product-uid
    install -Dm755 version ${D}${datadir}/updatehub/version
    install -Dm755 hardware ${D}${datadir}/updatehub/hardware
}

FILES:${PN} += "${datadir}/updatehub"

RDEPENDS:${PN} += " \
    os-release \
    updatehub-machine-info \
"
