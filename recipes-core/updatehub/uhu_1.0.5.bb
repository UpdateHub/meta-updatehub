SUMMARY = "updatehub utilities for update package management and server"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "file://0001-Revert-setup.py-Update-requests-requirement-for-2.20.patch"

SRC_URI[md5sum] = "9f7a94cbbc97aac9ee614d782e262a51"
SRC_URI[sha256sum] = "a9a95da05f098260df862c250dae94b9fc2c05ce6271e61004b027fb5e04b252"

inherit pypi setuptools3

CLEANBROKEN = "1"

do_install_append_class-native() {
     sed -i -e '1s|^#!.*|#!/usr/bin/env python3|' ${D}${bindir}/uhu
     create_wrapper ${D}${bindir}/uhu
}

# FIXME: The native package is not adding the runtime dependencies for
# the sysroot populate. The issue is reported on YOCTO: #10113
do_populate_sysroot[rdeptask] = "do_populate_sysroot"

# FIXME: Runtime dependency of python3-prompt-toolkit which is not handled
# for native case.  The issue is also related to YOCTO: #10113
RDEPENDS_${PN} += " \
    python3-wcwidth \
    python3-six \
"

RDEPENDS_${PN} += " \
    updatehub-package-schema \
    python3-certifi \
    python3-chardet \
    python3-click \
    python3-humanize \
    python3-idna \
    python3-jsonschema \
    python3-pycrypto \
    python3-progress \
    python3-prompt-toolkit \
    python3-requests \
    python3-rfc3987 \
    python3-urllib3 \
"

BBCLASSEXTEND = "native nativesdk"
