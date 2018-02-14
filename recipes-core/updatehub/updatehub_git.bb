SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=21acc3521d288a5d8a4b3fb611a39604"

DEPENDS_append = " glide-native libarchive upx-native"

GO_IMPORT = "github.com/updatehub/updatehub"
SRC_URI = " \
    git://${GO_IMPORT};protocol=https;destsuffix=${GO_IMPORT};branch=v1 \
    file://updatehub.initd \
    file://updatehub.service \
"

SRCREV = "45c5010430449df7c8e6611f646662c62a68f6a5"

PV = "1.0.3"

S = "${WORKDIR}/${GO_IMPORT}"

inherit go systemd update-rc.d pkgconfig

SYSTEMD_PACKAGE = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults 99"

UPX ?= "${STAGING_BINDIR_NATIVE}/upx"
UPX_ARGS ?= "--best -q"

do_configure() {
    mkdir -p ${S}/src
    GOPATH=${S}:${STAGING_LIBDIR}/${TARGET_SYS}/go glide install
}

do_compile() {
    export CGO_ENABLED=1
    GOPATH=${S}:${STAGING_LIBDIR}/${TARGET_SYS}/go oe_runmake build V=1
}

do_install() {
    # Copies the binaries to the target directory
    install -d ${D}${bindir}
    find "${S}/bin" ! -type d -print0 | xargs -r0 cp --target-directory="${D}${bindir}"
    chown -R root:root ${D}${bindir}

    # updatehub server udev rule for USB mounting
    install -Dm 0644 ${S}/cmd/updatehub-server/udev.rules \
                     ${D}${nonarch_base_libdir}/udev/rules.d/99-updatehub.rules

    # We don't want this shipped in target
    rm ${D}${bindir}/glide

    # Handle init system integration
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/updatehub.service ${D}${systemd_unitdir}/system/updatehub.service
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
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
