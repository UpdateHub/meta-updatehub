SUMMARY = "updatehub utilities for update package management and server"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pypi setuptools3

SRC_URI[md5sum] = "d3280e40c0dde60bd6adfca7f96bb8d0"
SRC_URI[sha256sum] = "61855684ecb2cbf76a6d2fdfc76151bb8624b586a1fc8d958d4b246daa3c838c"

CLEANBROKEN = "1"

do_install_append_class-native() {
     sed -i -e '1s|^#!.*|#!/usr/bin/env python3|' ${D}${bindir}/uhu
     create_wrapper ${D}${bindir}/uhu LC_ALL=C.UTF-8 LANG=C.UTF-8 LOCPATH=/usr/lib/locale
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
