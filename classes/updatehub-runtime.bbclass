# Handle UpdateHub agent runtime configuration
#
# This class is not intended to be used directly but through the
# updatehub-image class.
#
# The supported variables as well as their usage are documented on the
# updatehub-image class.
#
# Copyright 2016, 2017 (C) O.S. Systems Software LTDA.

UPDATEHUB_PACKAGE_VERSION ?= "${DISTRO_VERSION}"

UPDATEHUB_UHUPKG_PUBLIC_KEY ?= ""
UPDATEHUB_UHUPKG_PRIVATE_KEY ?= ""

UPDATEHUB_IMAGE_TYPE[type] = "list"
UPDATEHUB_IMAGE_TYPE[validitems] += "initramfs active/inactive"

UPDATEHUB_INSTALL_MODE ?= ""
UPDATEHUB_INSTALL_MODE[type] = "list"
UPDATEHUB_INSTALL_MODE[validitems] += "copy flash raw tarball ubifs imxkobs"

UPDATEHUB_FILESYSTEM_SUPPORT ?= ""
UPDATEHUB_FILESYSTEM_SUPPORT[type] = "list"
UPDATEHUB_FILESYSTEM_SUPPORT[validitems] += "btrfs ext2 ext3 ext4 f2fs jffs2 ubifs vfat xfs"

UPDATEHUB_ACTIVE_INACTIVE_BACKEND ?= ""
UPDATEHUB_ACTIVE_INACTIVE_BACKEND[type] = "list"
UPDATEHUB_ACTIVE_INACTIVE_BACKEND[validitems] += "u-boot grub grub-efi"

UPDATEHUB_DEVICE_IDENTITY ?= ""
UPDATEHUB_DEVICE_IDENTITY[type] = "list"
UPDATEHUB_DEVICE_IDENTITY[validitems] += "primary-iface cpuinfo-serial"

UPDATEHUB_DEVICE_ATTRIBUTE ?= ""
UPDATEHUB_DEVICE_ATTRIBUTE[type] = "list"
UPDATEHUB_DEVICE_ATTRIBUTE[validitems] += "kernel cpu-model mem-total"

UPDATEHUB_RUNTIME_PACKAGES = ""

python () {
    if bb.data.inherits_class('image', d):
        ### Ensures product uid is set
        product_uid = d.getVar("UPDATEHUB_PRODUCT_UID", False)
        if not product_uid:
            raise bb.parse.SkipRecipe("To enable UpdateHub support, the 'UPDATEHUB_PRODUCT_UID' variable must be set.")

        ### Ensure a version is set
        version = d.getVar("UPDATEHUB_PACKAGE_VERSION", False)

        ### Handle image type selection
        image_type = d.getVar("UPDATEHUB_IMAGE_TYPE", False)
        valid_image_types = d.getVarFlag('UPDATEHUB_IMAGE_TYPE', 'validitems', False).split()
        active_inactive_backend = d.getVar('UPDATEHUB_ACTIVE_INACTIVE_BACKEND', False)
        valid_active_inactive_backends = d.getVarFlag('UPDATEHUB_ACTIVE_INACTIVE_BACKEND', 'validitems', False).split()
        if not image_type:
            raise bb.parse.SkipRecipe("To enable UpdateHub support, the 'UPDATEHUB_IMAGE_TYPE' variable must be set. Valid image types are: %s" % ' '.join(valid_image_types))
        elif not image_type in valid_image_types:
            raise bb.parse.SkipRecipe("'%s' in UPDATEHUB_IMAGE_TYPE is not a valid image type. Valid image types are: %s" % (image_type, ' '.join(valid_image_types)))
        elif image_type in ['active/inactive']:
            if not active_inactive_backend:
                raise bb.parse.SkipRecipe("To enable UpdateHub with Active/Inactive image schema support, the 'UPDATEHUB_ACTIVE_INACTIVE_BACKEND' variable must be set. Valid backends are: %s" % ' '.join(valid_active_inactive_backends))

            d.appendVar('IMAGE_INSTALL', ' packagegroup-updatehub-active-inactive-runtime')
        elif image_type in ['initramfs']:
            if active_inactive_backend:
                raise bb.parse.SkipRecipe("To enable UpdateHub with initramfs image schema support, the 'UPDATEHUB_ACTIVE_INACTIVE_BACKEND' variable must NOT be set.")
            d.appendVar('IMAGE_INSTALL', ' packagegroup-updatehub-initramfs-support')

    ### Ensure a valid public key is provided
    uhupkg_public_key = d.getVar('UPDATEHUB_UHUPKG_PUBLIC_KEY', True)
    if not uhupkg_public_key:
        bb.warn("UpdateHub requires 'UPDATEHUB_UHUPKG_PUBLIC_KEY' variable to be set. The update system is not fully working on the generated images.")
    if uhupkg_public_key and not os.path.exists(uhupkg_public_key):
        raise bb.parse.SkipRecipe("The 'UPDATEHUB_UHUPKG_PUBLIC_KEY' variable must point to a existing file.")

    ### Ensure a valid private key is provided
    uhupkg_private_key = d.getVar('UPDATEHUB_UHUPKG_PRIVATE_KEY', True)
    if uhupkg_private_key and not os.path.exists(uhupkg_private_key):
        raise bb.parse.SkipRecipe("The 'UPDATEHUB_UHUPKG_PRIVATE_KEY' variable must point to a existing file.")

    ### Handle device identity selection
    device_identities = (d.getVar('UPDATEHUB_DEVICE_IDENTITY', True) or "").split()
    valid_device_identities = d.getVarFlag('UPDATEHUB_DEVICE_IDENTITY', 'validitems', False).split()
    if not device_identities:
        raise bb.parse.SkipRecipe("To enable UpdateHub the 'UPDATEHUB_DEVICE_IDENTITY' variable must be set. Valid device identities are: %s" % ' '.join(valid_device_identities))
    for device_identity in device_identities:
        d.appendVar('UPDATEHUB_RUNTIME_PACKAGES', ' updatehub-device-identity-%s' % device_identity)
        if not device_identity in valid_device_identities:
            raise bb.parse.SkipRecipe("'%s' in UPDATEHUB_DEVICE_IDENTITY is not valid. Valid device identities are: %s" % (device_identity, ' '.join(valid_device_identities)))


    ### Handle device attribute selection
    device_attributes = (d.getVar('UPDATEHUB_DEVICE_ATTRIBUTE', True) or "").split()
    valid_device_attributes = d.getVarFlag('UPDATEHUB_DEVICE_ATTRIBUTE', 'validitems', False).split()
    for device_attribute in device_attributes:
        d.appendVar('UPDATEHUB_RUNTIME_PACKAGES', ' updatehub-device-attribute-%s' % device_attribute)
        if not device_attribute in valid_device_attributes:
            raise bb.parse.SkipRecipe("'%s' in UPDATEHUB_DEVICE_ATTRIBUTE is not valid. Valid device attributes are: %s" % (device_attribute, ' '.join(valid_device_attributes)))

    ### Handle install mode selection
    modes = (d.getVar('UPDATEHUB_INSTALL_MODE', True) or "").split()
    valid_modes = d.getVarFlag('UPDATEHUB_INSTALL_MODE', 'validitems', False).split()
    mode_rdepends_map = {
        'copy'    : '',
        'flash'   : 'mtd-utils',
        'imxkobs' : 'imx-kobs',
        'raw'     : '',
        'tarball' : '',
        'ubifs'   : 'mtd-utils-ubifs',
    }
    if any(map(lambda x: x not in mode_rdepends_map.keys(), valid_modes)):
        raise bb.parse.SkipRecipe("Not all valid modes has the runtime dependencies mapped. Please check.")
    if not modes:
        raise bb.parse.SkipRecipe("To enable UpdateHub support, the 'UPDATEHUB_INSTALL_MODE' variable must be set. Valid modes are: %s" % ' '.join(valid_modes))
    else:
        for mode in modes:
            d.appendVar('UPDATEHUB_RUNTIME_PACKAGES', ' %s' % mode_rdepends_map[mode])
            if not mode in valid_modes:
                raise bb.parse.SkipRecipe("'%s' in UPDATEHUB_INSTALL_MODE is not a valid install mode. Valid install modes are: %s" % (mode, ' '.join(valid_modes)))

    ### Handle filesystem support selection
    filesystems = (d.getVar('UPDATEHUB_FILESYSTEM_SUPPORT', True) or "").split()
    valid_filesystems = d.getVarFlag('UPDATEHUB_FILESYSTEM_SUPPORT', 'validitems', False).split()

    filesystem_rdepends_map = {
        'btrfs': 'btrfs-tools',
        'ext2' : 'e2fsprogs-mke2fs',
        'ext3' : 'e2fsprogs-mke2fs',
        'ext4' : 'e2fsprogs-mke2fs',
        'f2fs' : 'f2fs-tools',
        'jffs2': 'mtd-utils-jffs2',
        'ubifs': 'mtd-utils-ubifs',
        'vfat' : 'dosfstools',
        'xfs'  : 'xfsprogs-mkfs',
    }

    if any(map(lambda x: x not in filesystem_rdepends_map.keys(), valid_filesystems)):
        raise bb.parse.SkipRecipe("Not all valid filesystems has the runtime dependencies mapped. Please check.")

    for filesystem in filesystems:
        if not filesystem in valid_filesystems:
            raise bb.parse.SkipRecipe("'%s' in UPDATEHUB_FILESYSTEM_SUPPORT is not a valid filesystem support. Valid filesystem supports are: %s" % (filesystem, ' '.join(valid_filesystems)))
        if any(mode in modes for mode in ['copy', 'tarball']):
            d.appendVar('UPDATEHUB_RUNTIME_PACKAGES', ' %s' % filesystem_rdepends_map[filesystem])
        else:
            bb.debug(1, "UPDATEHUB_FILESYSTEM_SUPPORT is set but there is no mode in use which makes use of it. Ignoring it.")

    ### Handle the Active/Inactive backend selection
    active_inactive_backend = d.getVar('UPDATEHUB_ACTIVE_INACTIVE_BACKEND', False)
    valid_active_inactive_backends = (d.getVarFlag('UPDATEHUB_ACTIVE_INACTIVE_BACKEND', 'validitems', False) or "").split()
    if active_inactive_backend and active_inactive_backend not in valid_active_inactive_backends:
        raise bb.parse.SkipRecipe("'%s' in UPDATEHUB_ACTIVE_INACTIVE_BACKEND is not a valid active/inactive backend. Valid active/inactive backends are: %s" % (active_inactive_backend, ' '.join(valid_active_inactive_backends)))
    elif active_inactive_backend:
        d.appendVar('UPDATEHUB_RUNTIME_PACKAGES', ' updatehub-active-inactive-backend-%s' % active_inactive_backend)
}
