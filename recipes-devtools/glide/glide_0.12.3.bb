SUMMARY = "Vendor Package Management for Golang"
HOMEPAGE = "https://glide.sh"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/LICENSE;md5=54905cf894f8cc416a92f4fc350c35b2"

DEPENDS = "go-masterminds-semver go-masterminds-vcs go-yaml go-urfave-cli"

GO_SRCROOT = "github.com/Masterminds/glide"
SRC_URI = "git://${GO_SRCROOT};protocol=https;branch=Release-branch-0.12.x \
           file://0001-Change-cli-package-to-refert-to-gopkg.in-urfave-cli..patch;patchdir=${GO_SRCROOT}"
SRCREV = "84607742b10f492430762d038e954236bbaf23f7"

inherit golang

do_compile_prepend() {
    rm -rf ${S}/${GO_SRCROOT}/vendor
}

FILES_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
