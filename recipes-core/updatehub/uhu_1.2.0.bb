SUMMARY = "updatehub utilities for update package management and server"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI[sha256sum] = "0976941d9941ecc0b6a1a8f419dd02e2f4e704d4364fb1185bdb4af7f3c7528c"

inherit pypi setuptools3

CLEANBROKEN = "1"

do_install:append:class-native() {
     sed -i -e '1s|^#!.*|#!/usr/bin/env python3|' ${D}${bindir}/uhu
     create_wrapper ${D}${bindir}/uhu LIBARCHIVE=${STAGING_LIBDIR_NATIVE}/libarchive.so.13
}

# FIXME: The native package is not adding the runtime dependencies for
# the sysroot populate. The issue is reported on YOCTO: #10113
do_populate_sysroot[rdeptask] = "do_populate_sysroot"

# FIXME: Runtime dependency of python3-prompt-toolkit which is not handled
# for native case.  The issue is also related to YOCTO: #10113
RDEPENDS:${PN} += " \
    python3-wcwidth \
    python3-six \
"

RDEPENDS:${PN} += " \
    updatehub-package-schema \
    python3-certifi \
    python3-chardet \
    python3-click \
    python3-humanize \
    python3-idna \
    python3-jsonschema \
    python3-pycryptodomex \
    python3-progress \
    python3-prompt-toolkit \
    python3-requests \
    python3-rfc3987 \
    python3-urllib3 \
    python3-libarchive-c \
"

BBCLASSEXTEND = "native nativesdk"
