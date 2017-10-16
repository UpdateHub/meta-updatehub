# Copyright (C) 2017 O.S. Systems Software LTDA.

SUMMARY = "updatehub initramfs image"

LICENSE = "MIT"

IMAGE_FSTYPES = "cpio.gz.u-boot"
IMAGE_ROOTFS_SIZE = "8192"
IMAGE_CLASSES = "image_types_uboot"

inherit core-image

# Avoid static /dev
USE_DEVFS = "1"

# Avoid locales installation
IMAGE_LINGUAS = ""

IMAGE_FEATURES += "read-only-rootfs"

# Avoid installation of busybox syslog and udhcpc
BAD_RECOMMENDATIONS += " \
    busybox-syslog \
    busybox-udhcpc \
"

UPDATEHUB_INITRAMFS_ESSENTIAL = " \
    ${VIRTUAL-RUNTIME_base-utils} \
    base-files \
    packagegroup-updatehub-initramfs-runtime \
"

CORE_IMAGE_BASE_INSTALL = " \
    ${UPDATEHUB_INITRAMFS_ESSENTIAL} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS} \
    \
    ${CORE_IMAGE_EXTRA_INSTALL} \
"
