# Copyright (C) 2012-2018 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

SRC_URI = "git://github.com/updatehub/u-boot-updatehub;branch=${SRCBRANCH}"

SRCREV = "27c607e93cb9114efe5128444933ad31af7d6cfd"
SRCBRANCH = "2018.09+updatehub"

PE = "1"
PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"
