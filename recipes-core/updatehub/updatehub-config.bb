SUMMARY = "UpdateHub configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
INHIBIT_DEFAULT_DEPS = "1"

inherit updatehub-runtime

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"

do_compile () {
    cat > updatehub.conf <<EOF
[Network]
DisableHttps=true
ServerAddress=${UPDATEHUB_SERVER_URL}

[Update]
SupportedInstallModes=${@', '.join(d.getVar('UPDATEHUB_INSTALL_MODE', False).split())}
EOF
}

do_install () {
    install -d ${D}${sysconfdir}
    install -m 0644 updatehub.conf ${D}${sysconfdir}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
