SUMMARY = "VCS Repository Management for Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE.txt;md5=834068240b54a555d06b98e4d717f277"

GO_IMPORT = "github.com/Masterminds/vcs"
SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT}"
SRCREV = "3084677c2c188840777bff30054f2b553729d329"

inherit go

BBCLASSEXTEND = "native nativesdk"
