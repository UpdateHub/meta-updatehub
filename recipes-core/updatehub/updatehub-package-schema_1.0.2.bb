SUMMARY = "updatehub - Package Schema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pypi setuptools3

SRC_URI[md5sum] = "f998eb2f01f2b91c527df0130ec1235a"
SRC_URI[sha256sum] = "22b277edfb0657c4a95063c4ad5be640ee7eadb788a3142c8446620199ba3e39"

CLEANBROKEN = "1"

RDEPENDS_${PN} += " \
    python3-rfc3987 \
    python3-jsonschema \
"

BBCLASSEXTEND = "native nativesdk"
