# Copyright 2026 (C) O.S. Systems Software LTDA.

SUMMARY = "Roll back an update which fails to validate itself"
DESCRIPTION = "Reboots the system when a freshly installed image does not \
validate itself within UPDATEHUB_VALIDATION_TIMEOUT, so U-Boot can count the \
boot attempt and roll back to the previously working image."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://${BPN} \
    file://${BPN}.service \
    file://${BPN}.timer \
"

S = "${WORKDIR}"

UPDATEHUB_VALIDATION_TIMEOUT ?= "5min"

# The timeout is baked into the installed files, so two machines configuring it
# differently must not share a package.
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Nothing is built; do not stage a cross toolchain for three text files.
INHIBIT_DEFAULT_DEPS = "1"

# The grace period is a systemd timer. Fail loudly rather than install units
# which no init system will ever run.
REQUIRED_DISTRO_FEATURES = "systemd"

inherit features_check systemd

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# The timer is what gets enabled; it pulls in the service when it elapses.
SYSTEMD_SERVICE:${PN} = "${BPN}.timer"

do_install() {
    install -Dm 0755 ${WORKDIR}/${BPN} ${D}${bindir}/${BPN}
    install -Dm 0644 ${WORKDIR}/${BPN}.service ${D}${systemd_system_unitdir}/${BPN}.service
    install -Dm 0644 ${WORKDIR}/${BPN}.timer ${D}${systemd_system_unitdir}/${BPN}.timer

    sed -i -e 's,@VALIDATION_TIMEOUT@,${UPDATEHUB_VALIDATION_TIMEOUT},g' \
           -e 's,@BINDIR@,${bindir},g' \
        ${D}${bindir}/${BPN} \
        ${D}${systemd_system_unitdir}/${BPN}.service \
        ${D}${systemd_system_unitdir}/${BPN}.timer
}

# systemd.bbclass doesn't follow the timer's Unit= to package the .service.
FILES:${PN} += "${systemd_system_unitdir}/${BPN}.service"

# fw_printenv, to read the boot counter state from the U-Boot environment.
RDEPENDS:${PN} += "u-boot-fw-utils"
