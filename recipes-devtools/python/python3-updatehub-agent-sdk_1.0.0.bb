require python-updatehub-agent-sdk.inc
inherit setuptools3

RDEPENDS_${PN} += " \
    python3-json \
    python3-urllib3 \
"
