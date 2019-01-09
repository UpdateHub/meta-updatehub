require python-updatehub-agent-sdk.inc
inherit setuptools

DEPENDS += "python-enum34-native"

RDEPENDS_${PN} += "python-enum34"
