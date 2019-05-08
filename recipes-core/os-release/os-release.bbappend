python () {
    if d.getVar('UPDATEHUB_PRODUCT_UID', False):
        d.appendVar('OS_RELEASE_FIELDS', ' UPDATEHUB_PRODUCT_UID')
}

VERSION_ID_append = "${UPDATEHUB_PACKAGE_VERSION_SUFFIX}"
