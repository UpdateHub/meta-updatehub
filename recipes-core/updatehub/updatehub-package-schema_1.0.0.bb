SUMMARY = "updatehub - Package Schema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pypi setuptools3

SRC_URI[md5sum] = "12ce6f38777d11239b15e037520e2fbb"
SRC_URI[sha256sum] = "09cb2e8cdc13ab472ad115cb124c16c301571d7d6680980ffdfdb4f5e9f05788"

CLEANBROKEN = "1"

RDEPENDS_${PN} += " \
    python3-rfc3987 \
    python3-jsonschema \
"

BBCLASSEXTEND = "native nativesdk"
