SUMMARY = "Simple, fast, and fun package for building command line apps in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=c542707ca9fc0b7802407ba62310bd8f"

DEPENDS_append = " go-burntsushi-toml go-yaml"

GO_IMPORT = "gopkg.in/urfave/cli.v1"
SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT}"
SRCREV = "0bdeddeeb0f650497d603c4ad7b20cfe685682f6"

inherit go

BBCLASSEXTEND = "native nativesdk"
