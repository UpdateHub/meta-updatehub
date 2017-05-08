SUMMARY = "Simple, fast, and fun package for building command line apps in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/LICENSE;md5=c542707ca9fc0b7802407ba62310bd8f"

DEPENDS = "go-burntsushi-toml go-yaml"

GO_SRCROOT = "gopkg.in/urfave/cli.v1"
SRC_URI = "git://${GO_SRCROOT};protocol=https"
SRCREV = "0bdeddeeb0f650497d603c4ad7b20cfe685682f6"

inherit golang

BBCLASSEXTEND = "native nativesdk"
