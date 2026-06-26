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

- Version title: `0.1.7-unofficial.1 for Minecraft 26.1.2`
- Version number: `0.1.7-unofficial.1+26.1.2`
- Environment: Client and server
- Loaders: Fabric
- Game versions: 26.1.2
- Dependencies:
  - Fabric API: required
  - Automobility Unofficial Port `0.5.0-unofficial.2` or newer: required
  - YACL: optional

## Upload File

- Local jar: `dist\momentum-0.1.7-unofficial.1+26.1.2-fabric.jar`
- Size: `82262`
- SHA-256: `0DDD8CF92F8CEA5A2BA6B92F116EFCD35B2526BB200D7ED01513BC40169E70E2`

## Changelog Text

Unofficial Fabric port of Momentum for Automobility to Minecraft Java 26.1.2.

### Added and Ported
- Ported Momentum's vehicle movement changes, braking, coasting, steering behavior, drift profiles, camera behavior, and minimal speed HUD to Minecraft 26.1.2.
- Ported client-to-server key state sync for brake and drift input on dedicated multiplayer servers.
- Ported the optional YACL config screen. It is available only when YACL is installed.
- Added cruise control on the `C` key while driving an Automobility vehicle.
- Added cruise control HUD indication to the existing Momentum speed HUD, including target speed and active/coasting color changes.

### Fixed
- Cruise control disengages when braking, drifting, reversing, leaving the driver seat, or hitting a wall/vehicle hard enough to count as an impact.
- Cruise throttle uses normal Automobility forward input instead of direct speed injection for multiplayer compatibility.

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
