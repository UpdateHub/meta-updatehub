# Reuse OE-Core recipe to avoid duplication of code
require recipes-core/images/core-image-minimal.bb

SUMMARY = "A small image just capable of allowing a device to boot and use the updatehub agent"

# Enable updatehub support
inherit updatehub-image
