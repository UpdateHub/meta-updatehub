SUMMARY = "UpdateHub utilities for update package management and server"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7d0c756730e640e1731676efa6782e9"

SRC_URI = "git://github.com/UpdateHub/uhu.git;protocol=https"
SRCREV = "c024b653cffa2fcc29d9aafb0bb53dbb3550fe3c"

PV = "1.0.0+${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools3

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
    python3-click \
    python3-progress \
    python3-requests \
    python3-rfc3987 \
    python3-jsonschema \
    python3-prompt-toolkit \
    python3-humanize \
"

BBCLASSEXTEND = "native nativesdk"
