SUMMARY = "UpdateHub configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "${@'file://${UPDATEHUB_UHUPKG_PUBLIC_KEY}' if '${UPDATEHUB_UHUPKG_PUBLIC_KEY}' else ''}"

inherit updatehub-runtime

do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"

UPDATEHUB_POLLING_INTERVAL ??= "1d"

UPDATEHUB_RUNTIME          ??= "${localstatedir}/lib/updatehub"
UPDATEHUB_RUNTIME_SETTINGS ??= "${UPDATEHUB_RUNTIME}/state.data"
UPDATEHUB_DOWNLOAD_DIR     ??= "${UPDATEHUB_RUNTIME}/download"

do_compile () {
    cat > updatehub.conf <<EOF
[Network]
ServerAddress=${UPDATEHUB_SERVER_URL}

[Storage]
RuntimeSettingsPath=${UPDATEHUB_RUNTIME_SETTINGS}

[Polling]
Interval=${UPDATEHUB_POLLING_INTERVAL}

[Update]
DownloadDir=${UPDATEHUB_DOWNLOAD_DIR}
SupportedInstallModes=${@', '.join(d.getVar('UPDATEHUB_INSTALL_MODE', False).split())}
EOF
}

do_install () {
    # Install the global configuration
    install -Dm 0644 updatehub.conf ${D}${sysconfdir}/updatehub.conf

    # Install the uhupkg public key
    if [ -n "${UPDATEHUB_UHUPKG_PUBLIC_KEY}" ]; then
        install -Dm 0644 ${UPDATEHUB_UHUPKG_PUBLIC_KEY} ${D}${datadir}/updatehub/key.pub
    fi
}

FILES_${PN} += "${datadir}/updatehub/key.pub"

PACKAGE_ARCH = "${MACHINE_ARCH}"
