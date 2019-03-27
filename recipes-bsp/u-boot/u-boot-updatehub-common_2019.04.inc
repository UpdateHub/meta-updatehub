# Copyright (C) 2012-2018 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

SRC_URI = "git://github.com/updatehub/u-boot-updatehub;branch=${SRCBRANCH}"

SRCREV = "92f28a3bbc841e5231c0f1c5be754f382f9bfdc8"
SRCBRANCH = "2019.04+updatehub"

PE = "1"
PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"
