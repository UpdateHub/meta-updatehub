SUMMARY = "UpdateHub's agent SDK for Python"
HOMEPAGE = "https://github.com/updatehub/agent-sdk-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI[sha256sum] = "9848f4e6c6d69b3c3080dd968dd5f2537cedcf46f0ece0db6d7485aa36abe8f1"

inherit pypi setuptools3

RDEPENDS_${PN} += " \
    python3-io \
    python3-json \
    python3-threading \
    python3-urllib3 \
    updatehub-sdk-statechange-trigger \
"
