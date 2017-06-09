SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=ba69bae29a956ebfb5d983fef66a2ea9"

DEPENDS_append = " glide-native libarchive"

GO_IMPORT = "github.com/UpdateHub/updatehub"
SRC_URI = " \
    git://${GO_IMPORT};protocol=https;destsuffix=${GO_IMPORT} \
    file://updatehub.initd \
    file://updatehub.service \
"

SRCREV = "f40dc9d9053bf9e42d21aff34ee082afd97aaf41"

PV = "0.0+${SRCPV}"

S = "${WORKDIR}/${GO_IMPORT}"

inherit go systemd update-rc.d

SYSTEMD_PACKAGE = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

INITSCRIPT_NAME = "${PN}"
INITSCRIPT_PARAMS = "defaults 99"

do_configure() {
    mkdir -p ${S}/src
    GOPATH=${S}:${STAGING_LIBDIR}/${TARGET_SYS}/go glide install
}

do_compile() {
    GOPATH=${S}:${STAGING_LIBDIR}/${TARGET_SYS}/go CGO_ENABLED=1 oe_runmake build V=1
}

do_install() {
    # Copies the binaries to the target directory
    install -d ${D}${bindir}
    find "${S}/bin" ! -type d -print0 | xargs -r0 cp --target-directory="${D}${bindir}"
    chown -R root:root ${D}${bindir}

    # We don't want this shipped in target
    rm ${D}${bindir}/glide

    # Handle init system integration
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -Dm 0644 ${WORKDIR}/updatehub.service ${D}${systemd_unitdir}/system/updatehub.service
    else
        install -Dm 0755 ${WORKDIR}/updatehub.initd ${D}/${sysconfdir}/init.d/updatehub
    fi
}

PACKAGES =+ "${PN}-server"
FILES_${PN}-server += "${bindir}/${PN}-server"

RDEPENDS_${PN}-dev += "bash"

BBCLASSEXTEND = "native nativesdk"
