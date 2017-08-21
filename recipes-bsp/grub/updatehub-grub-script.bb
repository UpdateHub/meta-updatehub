LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://grub.cfg"

inherit deploy

do_deploy () {
    install -Dm644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}/grub.cfg-${MACHINE}-${PV}-${PR}

    cd ${DEPLOYDIR}
    rm -f grub.cfg-${MACHINE} grub.cfg
    ln -sf grub.cfg-${MACHINE}-${PV}-${PR} grub.cfg-${MACHINE}
    ln -sf grub.cfg-${MACHINE}-${PV}-${PR} grub.cfg
}

addtask deploy after do_install before do_build

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"
