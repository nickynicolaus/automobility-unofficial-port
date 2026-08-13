# Modrinth Release - Automobility 0.5.0-unofficial.27 for 26.1.x

Use this file for the 26.1.x upload after the release candidate passes the in-game smoke test.

## Project

- Project: `https://modrinth.com/mod/automobility-unofficial-port`
- Environment: `Client and server, required on both`
- License: MIT
- Source: `https://github.com/nickynicolaus/automobility-unofficial-port`
- Issues: `https://github.com/nickynicolaus/automobility-unofficial-port/issues`

## Version Form

- Version type: `beta`
- Version number: `0.5.0-unofficial.27+26.1.2`
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

- Local JAR: `dist\release-candidate-0.5.0-unofficial.27\automobility-0.5.0-unofficial.27+26.1.2-fabric.jar`
- Size: `1356999` bytes
- SHA-256: `427D73F1C77805E0F49BD7621CB53601BF2BC4DC47C55C3339CFAE35C6E007DB`

## Version Changelog

```markdown
Translation update for the unofficial Minecraft Java 26.1.x Fabric port.

### Added
- Added German, Spanish, French, Hindi, Italian, Japanese, Brazilian Portuguese, Russian, Urdu (Pakistan), Vietnamese, and Traditional Chinese translations contributed through pull request #8.
- Added a complete Ukrainian translation using current Automobility keys and official Minecraft terminology for vanilla content.
- Added automated validation for translation JSON, known keys, and formatting placeholders.

### Fixed
- Restored numeric placeholders and the speed unit in the Hindi translation.
- Corrected wording in the German, Vietnamese, and Traditional Chinese translations.
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
