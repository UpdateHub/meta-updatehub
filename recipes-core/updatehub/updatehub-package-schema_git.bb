SUMMARY = "UpdateHub - Package Schema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/UpdateHub/package-schema.git;protocol=https"
SRCREV = "3d3c1dbc6336124f971d02c79b0636fdf127df64"

PV = "1.0.0+${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools3

CLEANBROKEN = "1"

RDEPENDS_${PN} += " \
    python3-rfc3987 \
    python3-jsonschema \
"

BBCLASSEXTEND = "native nativesdk"
