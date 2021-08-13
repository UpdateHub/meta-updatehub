SUMMARY = "Merge machine and updatehub options to create a runtime set"

LICENSE = "MIT"

# The package vary from one machine to another
PACKAGE_ARCH = "${MACHINE_ARCH}"

VIRTUAL-RUNTIME:updatehub-certificates ?= "ca-certificates"
VIRTUAL-RUNTIME:updatehub-config ?= "updatehub-config"
VIRTUAL-RUNTIME:updatehub-callbacks ?= "updatehub-callbacks"
VIRTUAL-RUNTIME:updatehub-system-version ?= "os-release"
VIRTUAL-RUNTIME:updatehub-system-inquiry ?= "updatehub-system-inquiry"
VIRTUAL-RUNTIME:updatehub-machine-info ?= "updatehub-machine-info"

inherit packagegroup updatehub-runtime

PACKAGES += "${PN}-initramfs-runtime ${PN}-initramfs-support ${PN}-active-inactive-runtime"

RDEPENDS:${PN}-active-inactive-runtime += " \
    ${VIRTUAL-RUNTIME:updatehub-certificates} \
    ${VIRTUAL-RUNTIME:updatehub-config} \
    ${VIRTUAL-RUNTIME:updatehub-callbacks} \
    ${VIRTUAL-RUNTIME:updatehub-machine-info} \
    ${VIRTUAL-RUNTIME:updatehub-system-version} \
    ${VIRTUAL-RUNTIME:updatehub-system-inquiry} \
    ${UPDATEHUB_RUNTIME_PACKAGES} \
    updatehub \
"

RDEPENDS:${PN}-initramfs-runtime += " \
    ${VIRTUAL-RUNTIME:updatehub-config} \
    ${VIRTUAL-RUNTIME:updatehub-callbacks} \
    ${VIRTUAL-RUNTIME:updatehub-machine-info} \
    ${VIRTUAL-RUNTIME:updatehub-system-inquiry} \
    ${UPDATEHUB_RUNTIME_PACKAGES} \
"

RDEPENDS:${PN}-initramfs-support += " \
    ${VIRTUAL-RUNTIME:updatehub-certificates} \
    ${VIRTUAL-RUNTIME:updatehub-config} \
    ${VIRTUAL-RUNTIME:updatehub-callbacks} \
    ${VIRTUAL-RUNTIME:updatehub-machine-info} \
    ${VIRTUAL-RUNTIME:updatehub-system-inquiry} \
    updatehub \
"
