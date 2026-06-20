# Modrinth Release Plan - Automobility 26.1.2 Port

Use this file as the source of truth for the first public Modrinth upload.

## Project Metadata

- Project type: Mod
- Current project URL: `https://modrinth.com/project/automobility-unofficial-port`
- Suggested project title: `Automobility Unofficial Port`
- Suggested slug: `automobility-unofficial-port`
- Summary: `Unofficial Fabric ports of FoundationGames' Automobility vehicle mod for newer Minecraft versions.`
- License: MIT
- Client side: Required
- Server side: Required
- Categories: Transportation, Technology, Equipment
- Loaders: Fabric
- Game versions: 26.1.2
- Release channel for first upload: Beta

Do not upload this as a version of the original Automobility project unless FoundationGames explicitly grants project access or asks for it.

## Required Links

Set these after creating the public fork/source repository:

- Source: `TODO: public fork URL`
- Issues: `TODO: public fork issue tracker URL`
- Wiki/Discord: leave empty unless there is a port-specific support location

Do not point fork-specific bug reports at upstream FoundationGames support channels.

## Version Metadata

- Version title: `0.5.0-unofficial.1 for Minecraft 26.1.2`
- Version number: `0.5.0-unofficial.1+26.1.2`
- Environment: Client and server
- Loaders: Fabric
- Game versions: 26.1.2
- Dependencies:
  - Fabric API: required
  - Fabric Loader 0.19.2 or newer: required
  - Controlify: optional

## Upload File

- Local jar: `dist\automobility-0.5.0-unofficial.1+26.1.2-fabric.jar`
- Size: `1372445`
- SHA-256: `233FC880F144B85002C758836F7D0159086D5AFE74AB686CFD7817DF2A3E4648`

## Changelog Text

Paste the `0.5.0-unofficial.1` section from `CHANGELOG.md`.

## Project Description Text

Paste `MODRINTH_DESCRIPTION.md` into the long project description.

## Gallery Assets

Use the existing images from the `md` directory:

- `md/banner.png`
- `md/construction.png`
- `md/parking.png`
- `md/driving.png`

## Pre-Publish Checklist

- Public source fork exists and contains `README.md`, `LICENSE`, `NOTICE.md`, and `CHANGELOG.md`.
- `README.md` clearly says this is an unofficial 26.1.2 port.
- Modrinth project description clearly says this is an unofficial fork/port.
- Modrinth links point to the fork, not to upstream support for fork bugs.
- Jar uploaded is the file listed above, not an older build from `fabric/build/libs`.
- Release channel is `Beta`.
- Dependencies include Fabric API as required.

## After Publish

- Save the Modrinth project URL and version URL.
- Download the uploaded jar from Modrinth once and compare SHA-256 with the hash above.
