SUMMARY = "UpdateHub's agent SDK for Python"
HOMEPAGE = "https://github.com/updatehub/agent-sdk-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b7633ba33f5204fb84beee30de6121e7"

SRC_URI[md5sum] = "8d32a1d1d505e8fd19eacd34b5f5bf40"
SRC_URI[sha256sum] = "537844c066f448c0a983ed8a106e009caac8b257fb2901fd5bc10b2548fbf611"

inherit pypi setuptools3

RDEPENDS_${PN} += " \
    python3-io \
    python3-json \
    python3-threading \
    python3-urllib3 \
    updatehub-sdk-statechange-trigger \
"
