SUMMARY = "A small image just capable of allowing a device to boot and use the updatehub agent"
LICENSE = "MIT"

inherit core-image updatehub-image

CORE_IMAGE_EXTRA_INSTALL += "updatehub-ctl"
