require recipes-bsp/u-boot/u-boot.inc
require u-boot-updatehub-common_${PV}.inc

DESCRIPTION = "U-Boot based fork for use with UpdateHub in \
order to provide support for some backported features and fixes, or because it \
was submitted for revision and it takes some time to become part of a stable \
version, or because it is not applicable for upstreaming."

DEPENDS += "bc-native bison-native dtc-native flex-native"

PROVIDES += "u-boot"

UBOOT_LOCALVERSION = "-updatehub+g${@d.getVar('SRCPV', True).partition('+')[2][0:7]}"

B = "${WORKDIR}/build"
do_configure[cleandirs] = "${B}"

# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"
