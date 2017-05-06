def __after_init_updatehub():
    PLATFORM_ROOT_DIR = os.environ['PLATFORM_ROOT_DIR']

    append_layers([ os.path.join(PLATFORM_ROOT_DIR, 'sources', p) for p in
                    [
                        'meta-openembedded/meta-oe',
                        'meta-openembedded/meta-python',
                        'meta-golang',
                        'meta-updatehub',
                    ]])

run_after_init(__after_init_updatehub)
