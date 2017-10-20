SUMMARY = "updatehub - Package Schema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/updatehub/package-schema.git;protocol=https"
SRCREV = "df61c4e2ff6a33ac457e4661d8a0423df462d518"

PV = "1.0.0+${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools3

CLEANBROKEN = "1"

RDEPENDS_${PN} += " \
    python3-rfc3987 \
    python3-jsonschema \
"

BBCLASSEXTEND = "native nativesdk"
