python () {
    if d.getVar('UPDATEHUB_PRODUCT_UID', False):
        d.appendVar('OS_RELEASE_FIELDS', ' UPDATEHUB_PRODUCT_UID')
}
