# Handle UpdateHub image integration
#
# Required variables:
#
#  UPDATEHUB_PRODUCT_UID
#
#  The UPDATEHUB_PRODUCT_UID identifies the product id in use. This is used by the UpdateHub server
#  to identify the product and track the possible versions for rollouts. The UPDATEHUB_PRODUCT_UID
#  is usually set on the distribution, as it is can be shared among multiple machines.
#
#  UPDATEHUB_UHUPKG_PUBLIC_KEY
#  UPDATEHUB_UHUPKG_PRIVATE_KEY
#
#  The UPDATEHUB_UHUPKG_PUBLIC_KEY and UPDATEHUB_UHUPKG_PRIVATE_KEY variables are required to point
#  to the keys which are used to validate and sign the update package.
#
#  The keys may or not be stored on the layer. Commonly the keys are not available for developers
#  and passed to the build system using the local.conf file of the autobuilder.
#
#  UPDATEHUB_IMAGE_TYPE
#
#  The UpdateHub can operate using different setup which can be chosen using the
#  UPDATEHUB_IMAGE_TYPE variable. It supports different values, as below:
#
#     initramfs
#
#     Enables the UpdateHub gold firmware support; this adds an initramfs based image which is used
#     for the upgrade process. In this mode, the UpdateHub agent is ran inside an initramfs image
#     which allows for the image to be changed without the need of a spare storage space.
#
#
#     active/inactive
#
#     Allow the use of active and inactive images schema. This reduces the downtime of the system
#     as the image can be change without rebooting. The new image is installed in a spare storage
#     area and in next reboot the new image is used. The UPDATEHUB_ACTIVE_INACTIVE_BACKEND variable
#     need to set depending of the machine requirement.
#
#  UPDATEHUB_ACTIVE_INACTIVE_BACKEND
#
#  The active and inactive image schema requires a backend to identify and choose the image to be
#  used for next boot. It supports: 'u-boot', 'grub' or 'grub-efi'.
#
#  UPDATEHUB_INSTALL_MODE
#
#  There are multiple installation modes supported. This is usually machine dependent as it depends
#  on the storate type in use. Supported values are: 'copy', 'flash', 'raw', 'tarball', 'ubifs' and
#  'imxkobs'.
#
#  UPDATEHUB_FILESYSTEM_SUPPORT
#
#  When using the 'copy' or 'tarball' installation mode, some filesystem support packages are
#  required. This variable controls which filesystems should be supported. It supports different
#  values, as 'btrfs', 'ext2', 'ext3', 'ext4', 'f2fs', 'jffs2', 'ubifs', 'vfat' and 'xfs'.
#
# Optional variables:
#
#  UPDATEHUB_SERVER_URL
#
#  Specifies the UpdateHub Server address to use. This is required in case you are running it
#  inside your private clould.
#
#  UPDATEHUB_ACCESS_ID
#  UPDATEHUB_ACCESS_SECRET
#
#  When using the uhupush task we can override the Access Id and the corresponding Secret for use.
#  This is usually used in auto builders as they may require different credentials depending on the
#  product being build.
#
#  UPDATEHUB_CUSTOM_CA_CERTS
#
#  Specify the CA certificate bundle to be used for uhupush task. It is currently used by UpdateHub
#  staging server for tests but may be interesting for other users when doing custom server
#  deployments.
#
# Copyright 2017-2020 (C) O.S. Systems Software LTDA.

inherit updatehub-image-tasks
