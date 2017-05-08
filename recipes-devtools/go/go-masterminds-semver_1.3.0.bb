SUMMARY = "Provides the ability to work with Semantic Versions in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/LICENSE.txt;md5=834068240b54a555d06b98e4d717f277"

GO_SRCROOT = "github.com/Masterminds/semver"
SRC_URI = "git://${GO_SRCROOT};protocol=https"
SRCREV = "abff1900528dbdaf6f3f5aa92c398be1eaf2a9f7"

inherit golang

BBCLASSEXTEND = "native nativesdk"
