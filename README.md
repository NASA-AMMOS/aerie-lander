![test](https://github.com/NASA-AMMOS/aerie-lander/actions/workflows/test.yml/badge.svg)

# Aerie Lander

A mission model of a lander type mission developed for the Merlin activity planning simulation environment.

## Contributing

Please read the [CONTRIBUTING.md](./CONTRIBUTING.md) guidelines for branch naming and pull request conventions.

## Building

To build `aerielander` against a local Aerie development repository the following Gradle `assemble` task must be invoked with `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables set:
```
GITHUB_ACTOR=<GH_ACTOR> \
GITHUB_TOKEN=<GH_TOKEN> \
gradle assemble \
    -PuseLocalAerie=true \
    -PlocalAeriePath=<PATH_TO_AERIE>
```
- `<GH_ACTOR>`: GitHub username.
- `<GH_TOKEN>`: GitHub token generated from https://github.com/settings/tokens ("Settings" > "Developer settings" > "Personal access tokens").
- `<PATH_TO_AERIE>`: path to local Aerie repository.
