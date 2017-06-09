SUMMARY = "YAML support for the Go language"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=6964839e54f4fefcdae13f22b92d0fbb"

GO_IMPORT = "gopkg.in/yaml.v2"

SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT}"
SRCREV = "cd8b52f8269e0feb286dfeef29f8fe4d5b397e0b"

inherit go

BBCLASSEXTEND = "native nativesdk"
