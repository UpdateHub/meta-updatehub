def __after_init_updatehub():
    PLATFORM_ROOT_DIR = os.environ['PLATFORM_ROOT_DIR']

    append_layers([ os.path.join(PLATFORM_ROOT_DIR, 'sources', p) for p in
                    [
                        'meta-openembedded/meta-networking',
                        'meta-openembedded/meta-oe',
                        'meta-openembedded/meta-python',
                        'oe-meta-go',
                        'meta-updatehub',
                    ]])

run_after_init(__after_init_updatehub)
