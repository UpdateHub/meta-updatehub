SUMMARY = "YAML support for the Go language"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/LICENSE;md5=6964839e54f4fefcdae13f22b92d0fbb"

GO_SRCROOT = "gopkg.in/yaml.v2"
SRC_URI = "git://${GO_SRCROOT};protocol=https"
SRCREV = "cd8b52f8269e0feb286dfeef29f8fe4d5b397e0b"

PV .= "+${SRCPV}"

inherit golang

BBCLASSEXTEND = "native nativesdk"
