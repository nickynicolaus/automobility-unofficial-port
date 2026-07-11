# Modrinth Release - Automobility 0.5.0-unofficial.26 for 26.1.x

Use this file for the 26.1.x upload after the release candidate passes the in-game smoke test.

## Project

- Project: `https://modrinth.com/mod/automobility-unofficial-port`
- Environment: `Client and server, required on both`
- License: MIT
- Source: `https://github.com/nickynicolaus/automobility-unofficial-port`
- Issues: `https://github.com/nickynicolaus/automobility-unofficial-port/issues`

## Version Form

- Version type: `beta`
- Version number: `0.5.0-unofficial.26+26.1.2`
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

- Local JAR: `dist\release-candidate-0.5.0-unofficial.26\automobility-0.5.0-unofficial.26+26.1.2-fabric.jar`
- Size: `1324901` bytes
- SHA-256: `F4949E3C68BA417C7D15A1FA8B1AAF94374A7EC19F7666CE97F097DD9DA0F965`

## Version Changelog

```markdown
Patch release for the unofficial Minecraft Java 26.1.x Fabric port.

### Fixed
- Preserved all autopilot heading vectors when routes are saved and loaded.
- Counted multiple copies in the same inventory stack correctly for Auto Mechanic Table recipes with repeated ingredients.
- Fixed the sticky-slope grace timer and block sampling at negative coordinates.
- Prevented a false first-tick vehicle collision measurement after an automobile is spawned or loaded.
- Guarded autopilot obstacle checks against stale hitboxes and clamped attachment animation states to valid values.

### Changed
- Limited periodic client state uploads to the locally driven automobile.
- Validated automobile state packet sizes and numeric values, capped packet size, and rate-limited accepted client syncs.
- Distributed automobile state through actual entity tracking and reduced visual state updates to the existing four-tick interpolation interval.
- Added automated regression tests for autopilot serialization, repeated recipe ingredients, and network state validation.
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
