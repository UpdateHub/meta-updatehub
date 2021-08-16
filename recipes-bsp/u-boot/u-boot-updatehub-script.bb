LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

### U-Boot Script for UpdateHub images
#
#  Bellow are documented all variables that you need to setup to use this
#  script, all listed variables are mandatory and must be properly set to
#  board boot properly.
#
#  Bear in mind that you don't need to append this recipe or create other,
#  just set the required variable in your machine configuration file, distro
#  file or local.conf.
#
#  Take care to add a escape character (\) before a $ symbol, we don't want
#  to evaluate the variable here. See the examples bellow.
#
# - UPDATEHUB_BOOTSCRIPT_LOAD_A and UPDATEHUB_BOOTSCRIPT_LOAD_B
#
#   Command to load kernel and dtb from first (system_a) and second (system_b)
#   rootfs partition, respectively.
#
#   You can add all the U-Boot commands that you need to load and prepare to
#   boot, e.g. run functions to find fdt file, get values from environment
#   variables and so on.
#
#   Note that for the example the second partition is system_a and third is system_b,
#   once the first partition is the one that bootscript is stored.
#
#     Examples:
#
#     UPDATEHUB_BOOTSCRIPT_LOAD_A:
#
#        load mmc \${devnum}:2 \${kernel_addr_r} boot/\${boot_file}; \
#        load mmc \${devnum}:2 \${fdt_addr_r} boot/\${fdt_file};
#
#     UPDATEHUB_BOOTSCRIPT_LOAD_B:
#
#      load mmc \${devnum}:3 \${kernel_addr_r} boot/\${boot_file}; \
#      load mmc \${devnum}:3 \${fdt_addr_r} boot/\${fdt_file};
#
#
# - UPDATEHUB_BOOTSCRIPT_FIND_ROOT_A and UPDATEHUB_BOOTSCRIPT_FIND_ROOT_B
#
#   Command to set part uuid used in root kernel cmdline for first and second
#   rootfs partition, respectively.
#
#     Examples:
#
#       UPDATEHUB_BOOTSCRIPT_FIND_ROOT_A:
#
#         part uuid mmc \${devnum}:2 uuid
#
#       UPDATEHUB_BOOTSCRIPT_FIND_ROOT_B:
#
#         part uuid mmc \${devnum}:3 uuid
#
#
# - UPDATEHUB_BOOTSCRIPT_BOOTARGS
#
#   Setup bootargs used in kernel command line. You need to set the root=
#   option to use PARTUUID and set this with value from $uuid, the is set by
#   UPDATEHUB_BOOTSCRIPT_FIND_ROOT_A/B.
#   In UPDATEHUB_BOOTSCRIPT_BOOTARGS you can add all the variables that board
#   need to boot properly.
#
#   In the following example we set vidargs that was already set in
#   U-Boot environment. Here you can set console, baud rate, extra args, etc.
#
#   Example:
#       root=PARTUUID=\${uuid} rw rootwait \${vidargs}
#
#
# - UPDATEHUB_BOOTSCRIPT_BEFORE_BOOTCMD
#
#   Allow inclusion of extra boot script fragment to be run before running the
#   UPDATEHUB_BOOTSCRIPT_BOOTCMD fragment, for example using `fdt` command to
#   enable/disable device tree handles.
#
#   Example:
#       fdt addr \${fdt_addr_r}; \
#       fdt resize; \
#       if itest.s "x" == "x\${lvds_size}"; then \
#           lvds_size="10"; \
#       fi \
#       \
#       if itest.s "x10" == "x\${lvds_size}"; then \
#           fdt set /panel compatible "aison,z101wx02jct736"; \
#       elif itest.s "x8" == "x\${lvds_size}"; then \
#           fdt set /panel compatible "aison,z080xg03jct3"; \
#       else
#           echo "ERROR: Unknown 'lvds_size'. Valid ones are '10' and '8'."; \
#       fi
#
#
# - UPDATEHUB_BOOTSCRIPT_BOOTCMD
#
#   Boot command to boot board after kernel and dtb are loaded, and initramfs if
#   you're using.
#
#   Example:
#
#     bootz \${kernel_addr_r} - \${fdt_addr_r}
#
#

DEPENDS = "u-boot-mkimage-native"

inherit deploy

python() {
    required_vars = [
        'UPDATEHUB_BOOTSCRIPT_LOAD_A', 'UPDATEHUB_BOOTSCRIPT_LOAD_B',
        'UPDATEHUB_BOOTSCRIPT_FIND_ROOT_A', 'UPDATEHUB_BOOTSCRIPT_FIND_ROOT_B',
        'UPDATEHUB_BOOTSCRIPT_BOOTARGS', 'UPDATEHUB_BOOTSCRIPT_BOOTCMD'
    ]

    for r in required_vars:
        var = d.getVar("%s" % r)
        if not var:
            raise bb.parse.SkipRecipe("%s variable must be set." % var)
}

UPDATEHUB_BOOTSCRIPT_BEFORE_BOOTCMD ?= ""

do_generate_bootscript() {
    cat > ${B}/boot.cmd <<EOF
if itest.s "x" == "x\${updatehub_active}" ; then
    echo Ensuring environment is accessible in Linux...
    setenv updatehub_active 0
    saveenv
fi

if itest.s "\${updatehub_active}" != 0 && itest.s "\${updatehub_active}" != 1; then
    echo Invalid updatehub_active value, resetting it
    setenv updatehub_active 0
    saveenv
fi

setenv bootargs ''

setenv updatehub_load_os_a "${UPDATEHUB_BOOTSCRIPT_LOAD_A}"
setenv updatehub_find_root_a "${UPDATEHUB_BOOTSCRIPT_FIND_ROOT_A}"

setenv updatehub_load_os_b "${UPDATEHUB_BOOTSCRIPT_LOAD_B}"
setenv updatehub_find_root_b "${UPDATEHUB_BOOTSCRIPT_FIND_ROOT_B}"

# We control the bootcount limit here to allow this logic to be
# changed inside the script, without storing more contents on
# the environment.
if itest.s "x" != "x\${bootcount}" && test \${bootcount} -ge 2; then
    if test \${updatehub_active} = 0; then
        echo Bootcount limit reached. Reverting to image B
        setenv updatehub_active 1
    else
        echo Bootcount limit reached. Reverting to image A
        setenv updatehub_active 0
    fi
    saveenv
fi;

# Find out where we should boot
if itest.s "\${updatehub_active}" == 0 ; then
    echo Loading system from A
    run updatehub_find_root_a updatehub_load_os_a
else
    echo Loading system from B
    run updatehub_find_root_b updatehub_load_os_b
fi

# Initialize the boot process
setenv bootargs "${UPDATEHUB_BOOTSCRIPT_BOOTARGS}"

# Extra bootscript fragments
${UPDATEHUB_BOOTSCRIPT_BEFORE_BOOTCMD}

# Run bootcmd
${UPDATEHUB_BOOTSCRIPT_BOOTCMD}
EOF
}
do_generate_bootscript[dirs] = "${B}"
do_generate_bootscript[vardeps] += " \
    UPDATEHUB_BOOTSCRIPT_LOAD_A UPDATEHUB_BOOTSCRIPT_LOAD_B \
    UPDATEHUB_BOOTSCRIPT_FIND_ROOT_A UPDATEHUB_BOOTSCRIPT_FIND_ROOT_B \
    UPDATEHUB_BOOTSCRIPT_BOOTARGS UPDATEHUB_BOOTSCRIPT_BOOTCMD \
    UPDATEHUB_BOOTSCRIPT_BEFORE_BOOTCMD \
"

addtask generate_bootscript after do_configure before do_mkimage

do_mkimage() {
    uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
                  -n "boot script" -d ${B}/boot.cmd ${B}/boot.scr
}
do_mkimage[dirs] = "${B}"
do_mkimage[file-checksums] = "${B}/boot.cmd"

addtask mkimage after do_generate_bootscript before do_install

do_install() {
    install -Dm 0644 ${B}/boot.scr ${D}/boot.scr
}

do_deploy() {
    install -Dm 0644 ${D}/boot.scr ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}
    cd ${DEPLOYDIR}
    rm -f boot.scr-${MACHINE} boot.scr
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr-${MACHINE}
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr
}

addtask deploy after do_install before do_build

FILES_${PN} += "/"

PROVIDES += "u-boot-default-script"
RPROVIDES_${PN} += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"
