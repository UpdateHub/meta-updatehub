SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE-APACHE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

DEPENDS = "libarchive openssl upx-native"

SRC_URI = " \
    git://github.com/UpdateHub/updatehub \
    file://updatehub-local-update \
    file://updatehub-local-update-systemd.rules \
    file://updatehub-local-update-sysvinit.rules \
    file://updatehub-local-update.service \
    file://updatehub.initd \
    file://updatehub.service \
"

SRCREV = "44e325b619340d95bdf4fa6124da41f66d99e5f8"

S = "${WORKDIR}/git/${BPN}"

PV = "1.1.90"

inherit cargo systemd update-rc.d pkgconfig

PACKAGECONFIG ?= "backward-compatibility"
PACKAGECONFIG[backward-compatibility] = "v1-parsing"

CARGO_FEATURES = "${PACKAGECONFIG_CONFARGS}"

SYSTEMD_PACKAGE = "${BPN}"
SYSTEMD_SERVICE_${BPN} = "${BPN}.service"

INITSCRIPT_NAME = "${BPN}"
INITSCRIPT_PARAMS = "defaults 99"

SYSTEMD_PACKAGE_updatehub-local-update = "updatehub-local-update"
SYSTEMD_SERVICE_updatehub-local-update = "updatehub-local-update@.service"
SYSTEMD_AUTO_ENABLE_updatehub-local-update = "disable"

UPX ?= "${STAGING_BINDIR_NATIVE}/upx"
UPX_ARGS ?= "--ultra-brute -q"

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

#apply_upx() {
#    ${UPX} ${UPX_ARGS} ${PKGDEST}/${BPN}/${bindir}/updatehub
#}

#PACKAGEFUNCS += "apply_upx"

PACKAGES =+ "${BPN}-local-update"

RDEPENDS_${BPN} += "openssl"

# Now, the same updatehub binary works as server and client tool, so replacing
# the old updatehub-ctl.
RREPLACES_${BPN} += "${BPN}-ctl"
RPROVIDES_${BPN} += "${BPN}-ctl"
RCONFLICTS_${BPN} += "${BPN}-ctl"

FILES_${BPN}-local-update += " \
    ${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules \
    ${systemd_system_unitdir}/updatehub-local-update@.service \
"

BBCLASSEXTEND = "native nativesdk"
