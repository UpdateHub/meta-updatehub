SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/src/${GO_IMPORT}/COPYING;md5=21acc3521d288a5d8a4b3fb611a39604"

DEPENDS_append = " glide-native libarchive upx-native"

GO_IMPORT = "github.com/updatehub/updatehub"
SRC_URI = " \
    git://${GO_IMPORT} \
    file://updatehub.initd \
    file://updatehub.service \
"

SRCREV = "66669fab367927c364fd83893eb5f82b6fe10c02"

PV = "0.0+${SRCPV}"

inherit go systemd update-rc.d pkgconfig

SYSTEMD_PACKAGE = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults 99"

UPX ?= "${STAGING_BINDIR_NATIVE}/upx"
UPX_ARGS ?= "--best -q"

do_configure_append() {
    cd ${S}/src/${GO_IMPORT}
    GOPATH=${B}:${STAGING_LIBDIR}/${TARGET_SYS}/go glide install
}

GO_INSTALL = " \
    ${GO_IMPORT}/cmd/updatehub \
    ${GO_IMPORT}/cmd/updatehub-ctl \
    ${GO_IMPORT}/cmd/updatehub-server \
"

GO_LINKMODE_append = " \
    -X main.gitversion=${PV} \
"

do_install_append() {
    # updatehub server udev rule for USB mounting
    install -Dm 0644 ${S}/src/${GO_IMPORT}/cmd/updatehub-server/udev.rules \
                     ${D}${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules

    # Handle init system integration
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/updatehub.service ${D}${systemd_unitdir}/system/updatehub.service
    else
        install -Dm 0755 ${WORKDIR}/updatehub.initd ${D}/${sysconfdir}/init.d/updatehub
    fi
}

apply_upx() {
    ${UPX} ${UPX_ARGS} ${PKGDEST}/${PN}/${bindir}/updatehub
    ${UPX} ${UPX_ARGS} ${PKGDEST}/${PN}-ctl/${bindir}/updatehub-ctl
    ${UPX} ${UPX_ARGS} ${PKGDEST}/${PN}-server/${bindir}/updatehub-server
}

PACKAGEFUNCS += "apply_upx"

PACKAGES =+ "${PN}-ctl ${PN}-server ${PN}-local-update"

FILES_${PN}-ctl += "${bindir}/${PN}-ctl"
FILES_${PN}-server += "${bindir}/${PN}-server"
FILES_${PN}-local-update += "${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules"
RDEPENDS_${PN}-local-update += "${PN}-server"

RDEPENDS_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
