HOMEPAGE = "http://upx.sourceforge.net"
SUMMARY = "Ultimate executable compressor."

SRC_URI = "https://github.com/upx/upx/releases/download/v${PV}/${BPN}-${PV}-src.tar.xz"

SRC_URI[md5sum] = "fa95336d9ddcaac3b494a1b6ae9d3557"
SRC_URI[sha256sum] = "3b0f55468d285c760fcf5ea865a070b27696393002712054c69ff40d8f7f5592"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=353753597aa110e0ded3508408c6374a"

DEPENDS = "zlib libucl xz"

S = "${WORKDIR}/${BPN}-${PV}-src"

EXTRA_OEMAKE += " \
    UPX_UCLDIR=${STAGING_DIR_TARGET} \
    UPX_LZMADIR=${STAGING_DIR_TARGET} \
"

do_compile() {
    oe_runmake -C src all
}

do_install_append() {
    install -d ${D}${bindir}
    install -m 755 ${B}/src/upx.out ${D}${bindir}/upx
}

BBCLASSEXTEND = "native"
