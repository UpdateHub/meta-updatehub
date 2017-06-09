SUMMARY = "Provides the ability to work with Semantic Versions in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE.txt;md5=834068240b54a555d06b98e4d717f277"

GO_IMPORT = "github.com/Masterminds/semver"
SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT}"
SRCREV = "abff1900528dbdaf6f3f5aa92c398be1eaf2a9f7"

inherit go

BBCLASSEXTEND = "native nativesdk"
