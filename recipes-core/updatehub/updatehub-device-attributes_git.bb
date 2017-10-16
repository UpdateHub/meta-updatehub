SUMMARY = "updatehub's device attributes scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/updatehub/device-attributes;protocol=https"
SRCREV = "b9fd983a9445f285355c82988835d2327887fbae"

S = "${WORKDIR}/git"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -Dm 0755 cpu_model ${D}/${datadir}/updatehub/device-attributes.d/cpu_model
    install -Dm 0755 kernel ${D}/${datadir}/updatehub/device-attributes.d/kernel
    install -Dm 0755 mem_total ${D}/${datadir}/updatehub/device-attributes.d/mem_total
}

python split_plugins_packages () {
    device_attribute_dir = d.expand('${datadir}/updatehub/device-attributes.d/')
    do_split_packages(d, device_attribute_dir, '^(.*)', 'updatehub-device-attribute-%s',
                      'updatehub %s device attribute script', extra_depends='')
}

PACKAGES_DYNAMIC = "updatehub-device-attribute-*"
PACKAGESPLITFUNCS_prepend = " split_plugins_packages "

PACKAGE_ARCH = "${MACHINE_ARCH}"
