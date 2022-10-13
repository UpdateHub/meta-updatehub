HOMEPAGE = "http://upx.sourceforge.net"
SUMMARY = "Ultimate executable compressor."


SRCREV_FORMAT = "upx_lzma-sdk"
SRCREV_lzma-sdk = "c6c2fc49e2ac6f3fc82762c6d3703969274d48de"
SRCREV_upx = "f88bfb36cd15ba5541e2fba256fce0b9f3d33835"

SRC_URI = " \
    git://github.com/upx/upx;protocol=https;branch=devel;name=upx \
    git://github.com/upx/upx-lzma-sdk;protocol=https;branch=master;destsuffix=git/src/lzma-sdk;name=lzma-sdk \
"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=353753597aa110e0ded3508408c6374a"

DEPENDS = "zlib libucl xz"

S = "${WORKDIR}/git"

PV = "3.96+${SRCPV}"

EXTRA_OEMAKE += " \
    UPX_UCLDIR=${STAGING_DIR_TARGET} \
    UPX_LZMADIR=${STAGING_DIR_TARGET} \
"

# FIXME: The build fails if security flags are enabled
SECURITY_CFLAGS = ""

do_compile() {
    oe_runmake -C src all
}

do_install:append() {
    install -d ${D}${bindir}
    install -m 755 ${B}/src/upx.out ${D}${bindir}/upx
}

BBCLASSEXTEND = "native"
