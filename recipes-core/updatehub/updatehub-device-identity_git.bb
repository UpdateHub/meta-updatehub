SUMMARY = "UpdateHub's device identity scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/UpdateHub/device-identity;protocol=https"
SRCREV = "81e58f80554d64b28f733f37f6b2b568c6bca007"

S = "${WORKDIR}/git"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0755 primary_iface ${D}/${datadir}/updatehub/device-identity.d/primary_iface
}

python split_plugins_packages () {
    device_identity_dir = d.expand('${datadir}/updatehub/device-identity.d/')
    do_split_packages(d, device_identity_dir, '^(.*)', 'updatehub-device-identity-%s', 'UpdateHub %s device identity script')
}

PACKAGES_DYNAMIC = "updatehub-device-identity-*"
PACKAGESPLITFUNCS_prepend = " split_plugins_packages "

PACKAGE_ARCH = "${MACHINE_ARCH}"
