PACKAGES =+ "util-linux-unshare"

SHARED_EXTRA_OECONF += "--enable-unshare"

FILES_util-linux-unshare = "${bindir}/unshare"
