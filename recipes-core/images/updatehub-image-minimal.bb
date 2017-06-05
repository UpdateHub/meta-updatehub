# Reuse OE-Core recipe to avoid duplication of code
require recipes-core/images/core-image-minimal.bb

SUMMARY = "A small image just capable of allowing a device to boot and use the UpdateHub agent"

# Enable UpdateHub support
inherit updatehub-image
