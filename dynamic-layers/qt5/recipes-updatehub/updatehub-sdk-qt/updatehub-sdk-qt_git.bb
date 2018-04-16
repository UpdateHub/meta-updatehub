DESCRIPTION = "updatehub - SDK for Qt/QML"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=838c366f69b72c5df05c96dff79b35f2"

PV = "1.0.2"

SRCREV = "779baccdf84e8363df97867b0db4633888683fc6"
SRC_URI = "git://github.com/updatehub/agent-sdk-qt.git"

S = "${WORKDIR}/git"

inherit qmake5

PACKAGECONFIG ??= "qmlplugin"
PACKAGECONFIG[qmlplugin] = "CONFIG+=qmlplugin,,qtdeclarative"

EXTRA_QMAKEVARS_PRE += " \
  PREFIX=${prefix} \
  ${PACKAGECONFIG_CONFARGS} \
"

RDEPENDS_${PN} += "updatehub-sdk-statechange-trigger"

PACKAGES += "${PN}-qmlplugin"

FILES_${PN}-qmlplugin += "${OE_QMAKE_PATH_QML}/updatehub"
RDEPENDS_${PN}-qmlplugin += "qtdeclarative-qmlplugins"
INSANE_SKIP_${PN}-qmlplugin = "dev-so"
