GRUB_BUILDIN_append = " loadenv test"

PACKAGES =+ "${PN}-editenv"
FILES_${PN}-editenv = "${bindir}/grub-editenv"
