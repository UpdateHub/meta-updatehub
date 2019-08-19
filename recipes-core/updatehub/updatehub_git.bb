SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/src/${GO_IMPORT}/LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

DEPENDS_append = " glide-native libarchive upx-native"

GO_IMPORT = "github.com/UpdateHub/updatehub"
SRC_URI = " \
    git://${GO_IMPORT};branch=v1 \
    file://updatehub-local-update \
    file://updatehub-local-update-systemd.rules \
    file://updatehub-local-update-sysvinit.rules \
    file://updatehub-local-update.service \
    file://updatehub.initd \
    file://updatehub.service \
"

SRCREV = "82a74f7b05b5b200b9f5caf611298dfa8a04db7d"

PV = "1.1.0"

inherit go glide systemd update-rc.d pkgconfig

# Avoid dynamic linking as it causes segfault
GO_LINKSHARED = ""

SYSTEMD_PACKAGE = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults 99"

SYSTEMD_PACKAGE_updatehub-local-update = "updatehub-local-update"
SYSTEMD_SERVICE_updatehub-local-update = "updatehub-local-update@.service"
SYSTEMD_AUTO_ENABLE_updatehub-local-update = "disable"

UPX ?= "${STAGING_BINDIR_NATIVE}/upx"
UPX_ARGS ?= "--best -q"

GO_INSTALL = " \
    ${GO_IMPORT}/cmd/updatehub \
    ${GO_IMPORT}/cmd/updatehub-ctl \
"

GO_LINKMODE_append = " \
    -X main.gitversion=${PV} \
"

do_install_append() {
    install -Dm 0755 ${WORKDIR}/updatehub-local-update ${D}${bindir}/updatehub-local-update

    # Handle init system integration and updatehub local update udev rule for USB mounting
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -Dm 0755 ${WORKDIR}/updatehub.initd ${D}${sysconfdir}/init.d/updatehub
        install -Dm 0644 ${WORKDIR}/updatehub-local-update-sysvinit.rules ${D}${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules
        sed -i -e 's,@BINDIR@,${bindir},g' \
            -e 's,@LIBDIR@,${libdir},g' \
            -e 's,@LOCALSTATEDIR@,${localstatedir},g' \
            -e 's,@SYSCONFDIR@,${sysconfdir},g' \
            ${D}/${sysconfdir}/init.d/updatehub
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/updatehub.service ${D}${systemd_system_unitdir}/updatehub.service
        install -Dm 0644 ${WORKDIR}/updatehub-local-update.service ${D}${systemd_system_unitdir}/updatehub-local-update@.service
        install -Dm 0644 ${WORKDIR}/updatehub-local-update-systemd.rules ${D}${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules
        sed -i -e 's,@BINDIR@,${bindir},g' \
            ${D}${systemd_system_unitdir}/updatehub.service \
            ${D}${systemd_system_unitdir}/updatehub-local-update@.service
    fi
}

apply_upx() {
    ${UPX} ${UPX_ARGS} ${PKGDEST}/${PN}/${bindir}/updatehub
    ${UPX} ${UPX_ARGS} ${PKGDEST}/${PN}-ctl/${bindir}/updatehub-ctl
}

PACKAGEFUNCS += "apply_upx"

PACKAGES =+ "${PN}-ctl ${PN}-local-update"

FILES_${PN}-ctl += "${bindir}/${PN}-ctl"
FILES_${PN}-local-update += " \
    ${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules \
    ${systemd_system_unitdir}/updatehub-local-update@.service \
"

RDEPENDS_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
