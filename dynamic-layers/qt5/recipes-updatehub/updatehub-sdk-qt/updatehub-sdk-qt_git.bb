DESCRIPTION = "updatehub - SDK for Qt/QML"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=838c366f69b72c5df05c96dff79b35f2"

PV = "1.0.3"

SRCREV = "0c01ccc7f2f061cad2b1d55f42404db45c0321a2"
SRC_URI = "git://github.com/updatehub/agent-sdk-qt.git"

S = "${WORKDIR}/git"

inherit qmake5

PACKAGECONFIG ??= "qmlplugin"
PACKAGECONFIG[qmlplugin] = "CONFIG+=qmlplugin,,qtdeclarative"

EXTRA_QMAKEVARS_PRE += " \
  PREFIX=${prefix} \
  ${PACKAGECONFIG_CONFARGS} \
"

RDEPENDS:${PN} += "updatehub-sdk-statechange-trigger"

PACKAGES += "${PN}-qmlplugin"

FILES:${PN}-qmlplugin += "${OE_QMAKE_PATH_QML}/updatehub"
RDEPENDS:${PN}-qmlplugin += "qtdeclarative-qmlplugins"
INSANE_SKIP:${PN}-qmlplugin = "dev-so"
