SUMMARY = "TOML parser and encoder for Go with reflection"
LICENSE = "WTFPL"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/COPYING;md5=389a9e29629d1f05e115f8f05c283df5"

GO_SRCROOT = "github.com/BurntSushi/toml"
SRC_URI = "git://${GO_SRCROOT};protocol=https"
SRCREV = "b26d9c308763d68093482582cea63d69be07a0f0"

inherit golang

BBCLASSEXTEND = "native nativesdk"
