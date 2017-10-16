SUMMARY = "updatehub's device identity scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/updatehub/device-identity;protocol=https"
SRCREV = "d32cb72abdd789ce28c66d87f4582ae07d465ee4"

S = "${WORKDIR}/git"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0755 primary_iface ${D}/${datadir}/updatehub/device-identity.d/primary_iface
    install -Dm 0755 cpuinfo_serial ${D}/${datadir}/updatehub/device-identity.d/cpuinfo_serial
}

python split_plugins_packages () {
    device_identity_dir = d.expand('${datadir}/updatehub/device-identity.d/')
    do_split_packages(d, device_identity_dir, '^(.*)', 'updatehub-device-identity-%s',
                      'updatehub %s device identity script', extra_depends='')
}

PACKAGES_DYNAMIC = "updatehub-device-identity-*"
PACKAGESPLITFUNCS_prepend = " split_plugins_packages "

PACKAGE_ARCH = "${MACHINE_ARCH}"
