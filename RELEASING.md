# Releasing

Releases are handled by [`sbt-ci-release`](https://github.com/olafurpg/sbt-ci-release) and [Github Actions](../.github/workflows/release.yml). Random versions are automatically deployed when changes are merged to main. To create a new semvar'ed version, do the following:

```bash
git tag -a v0.1.0
# add notes about what changed
git push origin v0.1.0
```
