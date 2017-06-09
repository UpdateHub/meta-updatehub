SUMMARY = "Vendor Package Management for Golang"
HOMEPAGE = "https://glide.sh"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=54905cf894f8cc416a92f4fc350c35b2"

DEPENDS_append = " go-masterminds-semver go-masterminds-vcs go-yaml go-urfave-cli"

GO_IMPORT = "github.com/Masterminds/glide"
SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT};branch=Release-branch-0.12.x \
           file://0001-Change-cli-package-to-refert-to-gopkg.in-urfave-cli..patch;patchdir=src/${GO_IMPORT}"
SRCREV = "84607742b10f492430762d038e954236bbaf23f7"

inherit go

do_compile_prepend() {
    rm -rf ${S}/src/${GO_IMPORT}/vendor
}

FILES_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
