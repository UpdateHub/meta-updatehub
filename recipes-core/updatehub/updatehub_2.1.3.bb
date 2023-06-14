# If this is git based prefer versioned ones if they exist
# DEFAULT_PREFERENCE = "-1"

# how to get updatehub could be as easy as but default to a git checkout:
# SRC_URI += "crate://crates.io/updatehub/2.1.3"
SRC_URI += "git://github.com/UpdateHub/updatehub.git;protocol=https;nobranch=1"
SRCREV = "dfe7ce9274cda1c54e0948eb0449cd90e6a27f33"
S = "${WORKDIR}/git"
CARGO_SRC_DIR = "updatehub"

# FIXME: update generateme with the real MD5 of the license file
LIC_FILES_CHKSUM = " \
    file://Apache-2.0;md5=generateme \
"

require updatehub-crates.inc

inherit cargo cargo-update-recipe-crates    

SUMMARY = "A Firmware Over-The-Air agent for Embedded and Industrial Linux-based devices"
HOMEPAGE = "https://updatehub.io/"
LICENSE = "Apache-2.0"

# includes this file if it exists but does not fail
# this is useful for anything you may want to override from
# what cargo-bitbake generates.
include updatehub.inc
