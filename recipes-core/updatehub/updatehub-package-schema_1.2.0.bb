SUMMARY = "updatehub - Package Schema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/UpdateHub/package-schema.git;protocol=https;branch=master"
SRCREV = "6379851de7e3667cbb6c7b0cc19e43edde74d415"

inherit setuptools3

CLEANBROKEN = "1"

RDEPENDS:${PN} += " \
    python3-rfc3987 \
    python3-jsonschema \
"

BBCLASSEXTEND = "native nativesdk"
