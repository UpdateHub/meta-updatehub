SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/COPYING;md5=ba69bae29a956ebfb5d983fef66a2ea9"

DEPENDS += "glide-native libarchive"

GO_SRCROOT = "github.com/UpdateHub/updatehub"
SRC_URI = "git://${GO_SRCROOT};protocol=https"
SRCREV = "d017bd0d140e6752c7228c3b0e6ecaa654b16dff"

PV = "0.0+${SRCPV}"

inherit golang

do_configure_prepend() {
    # This is required so glide call does work
    ln -snf ${S} ${B}/

    (cd ${S}/${GO_SRCROOT}; glide install)
}

do_compile() {
    (cd ${B}/src/${GO_SRCROOT}; ${GO} install ${GO_LINKSHARED} ${GOBUILDFLAGS} ./)
}

RDEPENDS_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
