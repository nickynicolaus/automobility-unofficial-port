# Modrinth Release Plan - Momentum for Automobility 26.1.2 Port

Use this file as the source of truth for the first public Modrinth upload of the Momentum addon port.

## Project Metadata

- Project type: Mod
- Suggested project title: `Momentum for Automobility Unofficial Port`
- Suggested slug: `momentum-for-automobility-unofficial-port`
- Summary: `Unofficial Fabric port of Momentum for Automobility for newer Minecraft versions.`
- License: MIT
- Client side: Required
- Server side: Required
- Categories: Transportation, Utility
- Loaders: Fabric
- Game versions: 26.1.2
- Release channel for first upload: Beta

Do not upload this as a version of the original Momentum project unless the upstream maintainer explicitly grants project access or asks for it.

## Required Links

- Source: `https://github.com/nickynicolaus/automobility-unofficial-port`
- Issues: `https://github.com/nickynicolaus/automobility-unofficial-port/issues`
- Wiki/Discord: leave empty unless there is a port-specific support location

## Version Metadata

- Version title: `0.1.5-unofficial.1 for Minecraft 26.1.2`
- Version number: `0.1.5-unofficial.1+26.1.2`
- Environment: Client and server
- Loaders: Fabric
- Game versions: 26.1.2
- Dependencies:
  - Fabric API: required
  - Automobility Unofficial Port `0.5.0-unofficial.2` or newer: required
  - YACL: optional

## Upload File

- Local jar: `dist\momentum-0.1.5-unofficial.1+26.1.2-fabric.jar`
- Size: `76189`
- SHA-256: `CD38A7504D1F84094821F0B284D487EFA5F7945D5BF87D401B368E28AA17D823`

## Changelog Text

Initial unofficial Fabric port of Momentum for Automobility to Minecraft Java 26.1.2.

### Added and Ported
- Ported Momentum's vehicle movement changes, braking, coasting, steering behavior, drift profiles, camera behavior, and minimal speed HUD to Minecraft 26.1.2.
- Ported client-to-server key state sync for brake and drift input on dedicated multiplayer servers.
- Ported the optional YACL config screen. It is available only when YACL is installed.

### Known Limits
- ModMenu integration is not included in this build because the available ModMenu API artifact for this environment is not compatible with the named Loom dependency setup used by this port.
- This addon requires Automobility `0.5.0-unofficial.2` or newer on both client and server.

## Project Description Text

`Momentum for Automobility Unofficial Port` is an unofficial Fabric port of Momentum for Automobility by milkucha for Minecraft Java 26.1.2.

Momentum changes Automobility vehicle movement feel and adds a minimal speed HUD. It requires Automobility and must be installed on both client and server for multiplayer.

This project is not affiliated with or endorsed by the upstream Momentum maintainer unless stated otherwise by that maintainer. Bugs specific to this 26.1.2 port should be reported to this fork.

## Pre-Publish Checklist

- Public source fork contains the `momentum` subproject.
- README and NOTICE mention the upstream Momentum project and license.
- Modrinth links point to this fork, not to upstream support for fork bugs.
- Jar uploaded is the file listed above.
- Release channel is `Beta`.
- Dependencies include Fabric API and Automobility Unofficial Port as required.

## After Publish

- Save the Modrinth project URL and version URL.
- Download the uploaded jar from Modrinth once and compare SHA-256 with the hash above.
