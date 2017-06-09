SUMMARY = "TOML parser and encoder for Go with reflection"
LICENSE = "WTFPL"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/COPYING;md5=389a9e29629d1f05e115f8f05c283df5"

GO_IMPORT = "github.com/BurntSushi/toml"
SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT}"
SRCREV = "b26d9c308763d68093482582cea63d69be07a0f0"

inherit go

BBCLASSEXTEND = "native nativesdk"
