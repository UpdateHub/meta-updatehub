SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${GO_SRCROOT}/COPYING;md5=ba69bae29a956ebfb5d983fef66a2ea9"

DEPENDS += "glide-native libarchive"

GO_SRCROOT = "github.com/UpdateHub/updatehub"
SRC_URI = " \
    git://${GO_SRCROOT};protocol=https \
    file://updatehub.initd \
    file://updatehub.service \
"

SRCREV = "f40dc9d9053bf9e42d21aff34ee082afd97aaf41"

PV = "0.0+${SRCPV}"

inherit golang systemd update-rc.d

SYSTEMD_PACKAGE = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults 99"

do_configure_prepend() {
    # This is required so glide call does work
    ln -snf ${S} ${B}/

    (cd ${S}/${GO_SRCROOT}; glide install)
}

do_compile() {
    (cd ${B}/src/${GO_SRCROOT}; oe_runmake build)
}

do_install() {
    # Copies the binaries to the target directory
    install -d ${D}${bindir}
    install -m 755 ${B}/src/${GO_SRCROOT}/bin/* ${D}${bindir}/

    # Handle init system integration
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${S}/updatehub.service ${D}${systemd_unitdir}/system/updatehub.service
    else
        install -Dm 0755 ${S}/updatehub.initd ${D}/${sysconfdir}/init.d/updatehub
    fi
}

PACKAGES =+ "${PN}-server"
FILES_${PN}-server += "${bindir}/${PN}-server"

RDEPENDS_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
