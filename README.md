# OpenEmbedded/Yocto Project support layer for UpdateHub

UpdateHub is an enterprise-grade solution which makes simple to remotely update
all your Linux-based devices in the field. It handles all aspects related to
sending Firmware Over-the-Air (FOTA) updates with maximum security and efficiency,
while you focus in adding value to your product.

To learn more about UpdateHub, check out our [documentation](https://docs.updatehub.io).

## Dependencies

This layer depends on:

```shell
  URI: git://git.openembedded.org/bitbake
  branch: master

  URI: git://git.openembedded.org/openembedded-core
  layers: meta
  branch: master

  URI: git://github.com/openembedded/meta-openembedded.git
  subdirectory: meta-oe
  branch: master

  URI: git://github.com/openembedded/meta-openembedded.git
  subdirectory: meta-python
  branch: master
```

## Contributing

UpdateHub is an open source project and we love to receive contributions from our community.
If you would like to contribute, please read our [contributing guide](https://github.com/UpdateHub/updatehub/blob/v1/CONTRIBUTING.md).

## License

UpdateHub Yocto Project Reference Platform is licensed under the MIT License.
See [COPYING.MIT](COPYING.MIT) for the full license text.

## Getting in touch

* Reach us on [Gitter](https://gitter.im/UpdateHub/community)
* All source code are in [Github](https://github.com/UpdateHub)
* Email us at [contact@updatehub.io](mailto:contact@updatehub.io)
