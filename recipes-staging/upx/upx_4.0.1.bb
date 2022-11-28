HOMEPAGE = "http://upx.sourceforge.net"
SUMMARY = "Ultimate executable compressor."

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=353753597aa110e0ded3508408c6374a"

inherit pkgconfig cmake

SRC_URI = " \
    https://github.com/upx/upx/releases/download/v${PV}/upx-${PV}-src.tar.xz \
"

SRC_URI[sha256sum] = "77003c8e2e29aa9804e2fbaeb30f055903420b3e01d95eafe01aed957fb7e190"

DEPENDS = "zlib libucl xz"

S = "${WORKDIR}/upx-${PV}-src"

BBCLASSEXTEND = "native"
