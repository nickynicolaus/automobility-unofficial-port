# Modrinth Release - Automobility 0.5.0-unofficial.28 for 26.1.x

Use this file for the 26.1.x upload after the release candidate passes the in-game smoke test.

## Project

- Project: `https://modrinth.com/mod/automobility-unofficial-port`
- Environment: `Client and server, required on both`
- License: MIT
- Source: `https://github.com/nickynicolaus/automobility-unofficial-port`
- Issues: `https://github.com/nickynicolaus/automobility-unofficial-port/issues`

## Version Form

- Version type: `beta`
- Version number: `0.5.0-unofficial.28+26.1.2`
- Version subtitle: `for Minecraft 26.1.x`
- Loaders: `Fabric`
- Game versions: `26.1`, `26.1.1`, `26.1.2`
- Environment: `Client and server, required on both`

## Dependencies

- Fabric API: required
- Controlify (Controller support): optional
- Momentum for Automobility: Unofficial Port: optional

Do not add Fabric Loader as a project dependency. The JAR metadata already requires Fabric Loader 0.19.2 or newer.

## Upload File

- Local JAR: `dist\release-candidate-0.5.0-unofficial.28\automobility-0.5.0-unofficial.28+26.1.2-fabric.jar`
- Size: `1365101` bytes
- SHA-256: `CCD31AFB6FED0E2FD4A78C2A4B2273B84940AB5680284F3CDA548C6A147B3089`

## Version Changelog

```markdown
Translation update for the unofficial Minecraft Java 26.1.x Fabric port.

### Added
- Added complete Czech, Polish, and Korean translations using current Automobility keys and official Minecraft terminology for vanilla content.

### Fixed
- Completed the Simplified Chinese translation and replaced obsolete MidnightControls keys with the current Controlify bindings.
- Made translation validation require every locale to match the complete English key set while preserving formatting placeholders.
```

## Before Upload

- Complete the 26.1.2 singleplayer and multiplayer smoke test.
- Push the `main` branch and create the matching GitHub release.
- Upload exactly the JAR and metadata listed above.
- Keep the version as beta while the port still has limited public testing.
- Use `MODRINTH_DESCRIPTION.md` as the project description.

## After Upload

- Download the public Modrinth file and compare its SHA-256 with the value above.
- Confirm Modrinth shows Fabric and all three 26.1.x game versions.
- Confirm Fabric API is required and both Controlify and Momentum are optional.
