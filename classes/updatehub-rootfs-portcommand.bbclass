# Allow overriding of UpdateHub Product UID
#
# Required variable:
#
#  UPDATEHUB_ROOTFS_PRODUCT_UID
#
#  The UPDATEHUB_ROOTFS_PRODUCT_UID allow for override of existing UpdateHub
#  Product UID during rootfs generation.
#
# Copyright 2021 (C) O.S. Systems Software LTDA.


# Override the UPDATEHUB_PRODUCT_UID from existing os-release configuration file.
ROOTFS_POSTPROCESS_COMMAND += 'updatehub_override_product_uid ;'
updatehub_override_product_uid () {
    if [ ! -e "${IMAGE_ROOTFS}${sysconfdir}/os-release" ]; then
        bbfatal "'${sysconfdir}/os-release' doesn't exist."
    fi

    if [ -z "${UPDATEHUB_ROOTFS_PRODUCT_UID}" ]; then
        bbfatal "UPDATEHUB_ROOTFS_PRODUCT_UID variable is undefined."
    fi

    sed -i 's%^UPDATEHUB_PRODUCT_UID=.*%UPDATEHUB_PRODUCT_UID="${UPDATEHUB_ROOTFS_PRODUCT_UID}"%g' \
        ${IMAGE_ROOTFS}${sysconfdir}/os-release
}
