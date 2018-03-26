SUMMARY = "Merge machine and updatehub options to create a runtime set"

LICENSE = "MIT"

# The package vary from one machine to another
PACKAGE_ARCH = "${MACHINE_ARCH}"

VIRTUAL-RUNTIME_updatehub-certificates ?= "ca-certificates"
VIRTUAL-RUNTIME_updatehub-config ?= "updatehub-config"
VIRTUAL-RUNTIME_updatehub-callbacks ?= "updatehub-callbacks"
VIRTUAL-RUNTIME_updatehub-system-version ?= "os-release"
VIRTUAL-RUNTIME_updatehub-system-inquiry ?= "updatehub-system-inquiry"
VIRTUAL-RUNTIME_updatehub-machine-info ?= "updatehub-machine-info"

inherit packagegroup updatehub-runtime

PACKAGES += "${PN}-initramfs-runtime ${PN}-initramfs-support ${PN}-active-inactive-runtime"

RDEPENDS_${PN}-active-inactive-runtime += " \
    ${VIRTUAL-RUNTIME_updatehub-certificates} \
    ${VIRTUAL-RUNTIME_updatehub-config} \
    ${VIRTUAL-RUNTIME_updatehub-callbacks} \
    ${VIRTUAL-RUNTIME_updatehub-machine-info} \
    ${VIRTUAL-RUNTIME_updatehub-system-version} \
    ${VIRTUAL-RUNTIME_updatehub-system-inquiry} \
    ${UPDATEHUB_RUNTIME_PACKAGES} \
    updatehub \
"

RDEPENDS_${PN}-initramfs-runtime += " \
    ${VIRTUAL-RUNTIME_updatehub-config} \
    ${VIRTUAL-RUNTIME_updatehub-callbacks} \
    ${VIRTUAL-RUNTIME_updatehub-machine-info} \
    ${VIRTUAL-RUNTIME_updatehub-system-inquiry} \
    ${UPDATEHUB_RUNTIME_PACKAGES} \
"

RDEPENDS_${PN}-initramfs-support += " \
    ${VIRTUAL-RUNTIME_updatehub-certificates} \
    ${VIRTUAL-RUNTIME_updatehub-config} \
    ${VIRTUAL-RUNTIME_updatehub-callbacks} \
    ${VIRTUAL-RUNTIME_updatehub-machine-info} \
    ${VIRTUAL-RUNTIME_updatehub-system-inquiry} \
    updatehub \
"
