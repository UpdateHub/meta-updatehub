SUMMARY = "updatehub configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "${@'file://${UPDATEHUB_UHUPKG_PUBLIC_KEY}' if '${UPDATEHUB_UHUPKG_PUBLIC_KEY}' else ''}"

inherit updatehub-runtime

do_patch[noexec] = "1"
do_configure[noexec] = "1"

UPDATEHUB_POLLING_ENABLED  ??= "true"
UPDATEHUB_POLLING_INTERVAL ??= "1d"

UPDATEHUB_RUNTIME          ??= "${localstatedir}/lib/updatehub"
UPDATEHUB_RUNTIME_SETTINGS ??= "/data/updatehub/state.data"
UPDATEHUB_DOWNLOAD_DIR     ??= "${UPDATEHUB_RUNTIME}/download"

do_compile () {
    cat > updatehub.conf <<EOF
[network]
server_address = "${UPDATEHUB_SERVER_URL}"
listen_socket = "localhost:8080"

[storage]
read_only = false
runtime_settings = "${UPDATEHUB_RUNTIME_SETTINGS}"

[polling]
enabled = ${UPDATEHUB_POLLING_ENABLED}
interval = "${UPDATEHUB_POLLING_INTERVAL}"

[update]
download_dir = "${UPDATEHUB_DOWNLOAD_DIR}"
supported_install_modes = ${@d.getVar('UPDATEHUB_INSTALL_MODE', False).split()}

[firmware]
metadata="/usr/share/updatehub"
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

FILES:${PN} += "${datadir}/updatehub/key.pub"

PACKAGE_ARCH = "${MACHINE_ARCH}"
