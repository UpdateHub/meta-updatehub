SUMMARY = "updatehub - Package Schema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pypi setuptools3

SRC_URI[md5sum] = "2ad45062e9baba7a764cbbe531621d75"
SRC_URI[sha256sum] = "1f541bc006f1d65854103550d42f0ee09a74424b8fe72717c461d47bf1548be2"

CLEANBROKEN = "1"

RDEPENDS_${PN} += " \
    python3-rfc3987 \
    python3-jsonschema \
"

BBCLASSEXTEND = "native nativesdk"
