require python-updatehub-agent-sdk.inc
inherit setuptools3

RDEPENDS_${PN} += " \
    python3-enum \
    python3-json \
    python3-selectors \
    python3-signal \
    python3-urllib3 \
"
