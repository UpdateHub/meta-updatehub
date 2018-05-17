SUMMARY = "UpdateHub's agent SDK for Python"
HOMEPAGE = "https://github.com/updatehub/agent-sdk-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b7633ba33f5204fb84beee30de6121e7"

DEPENDS = "${PYTHON_PN}-setuptools"

inherit pypi

SRC_URI[md5sum] = "f9ede04bfb9fbd3a4ba74929379368a7"
SRC_URI[sha256sum] = "aface0f287dcba235632c8b86e5afc46b07d51fd73a98c5c48f2b664d3dc6aa8"

RDEPENDS_${PN} += " \
    ${PYTHON_PN}-io \
    ${PYTHON_PN}-threading \
    updatehub-sdk-statechange-trigger \
"
