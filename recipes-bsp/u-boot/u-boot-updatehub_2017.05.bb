require recipes-bsp/u-boot/u-boot.inc
require u-boot-updatehub-common_${PV}.inc

INSANE_SKIP_${PN} += "ldflags textrel"
