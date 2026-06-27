# Modrinth Release Plan - Momentum for Automobility 26.1.x Port

Use this file as the source of truth for the first public Modrinth upload of the Momentum addon port.

## Project Metadata

- Project type: Mod
- Suggested project title: `Momentum for Automobility: Unofficial Port`
- Suggested slug: `momentum-for-automobility-unofficial-port`
- Summary: `Handling tweaks, cruise control, and a speed HUD for Automobility on Minecraft 26.1.x Fabric.`
- License: MIT
- Client side: Required
- Server side: Required
- Categories: Transportation, Utility
- Loaders: Fabric
- Game versions: 26.1, 26.1.1, 26.1.2
- Release channel for first upload: Beta

Do not upload this as a version of the original Momentum project unless the upstream maintainer explicitly grants project access or asks for it.

## Required Links

- Source: `https://github.com/nickynicolaus/automobility-unofficial-port`
- Issues: `https://github.com/nickynicolaus/automobility-unofficial-port/issues`
- Wiki/Discord: leave empty unless there is a port-specific support location

## Version Metadata

- Version title: `0.1.11-unofficial.3 for Minecraft 26.1.x`
- Version number: `0.1.11-unofficial.3+26.1.2`
- Environment: Client and server
- Loaders: Fabric
- Game versions: 26.1, 26.1.1, 26.1.2
- Dependencies:
  - Fabric API: required
  - Automobility Unofficial Port `0.5.0-unofficial.2` or newer: required
  - YACL: optional

## Upload File

- Local jar: `dist\momentum-0.1.11-unofficial.3+26.1.2-fabric.jar`
- Size: `82249`
- SHA-256: `f151eed6d4a5065c7e85b27e24bd26b4045fc7125841c2b44c19749733c5408f`

## Changelog Text

Unofficial Fabric port of Momentum for Automobility to Minecraft Java 26.1.x. This build was smoke-tested on Minecraft Java 26.1.2.

### Added and Ported
- Ported Momentum's vehicle movement changes, braking, coasting, steering behavior, drift profiles, camera behavior, and minimal speed HUD to Minecraft 26.1.x.
- Ported client-to-server key state sync for brake and drift input on dedicated multiplayer servers.
- Ported the optional YACL config screen. It is available only when YACL is installed.
- Added cruise control on the `C` key while driving an Automobility vehicle.
- Added cruise control HUD indication to the existing Momentum speed HUD, including target speed and active/coasting color changes.

### Fixed
- Cruise control disengages when braking, drifting, reversing, leaving the driver seat, or hitting a wall/vehicle hard enough to count as an impact.
- Cruise throttle uses normal Automobility forward input instead of direct speed injection for multiplayer compatibility.

### Changed
- Replaced the procedural cruise control HUD glyph with a dedicated texture icon.
- Increased the cruise control HUD icon size and added a procedural fallback so the cruise indicator remains visible if texture rendering fails in the HUD layer.
- Replaced the cruise control HUD icon with compact procedural chevrons that match the existing speed HUD.
- Reduced the cruise control HUD chevron size and made the active cruise color stable instead of switching with throttle/coast state.

### Known Limits
- ModMenu integration is not included in this build because the available ModMenu API artifact for this environment is not compatible with the named Loom dependency setup used by this port.
- This addon requires Automobility `0.5.0-unofficial.2` or newer on both client and server.

## Project Description Text

Momentum for Automobility is a small handling addon for Automobility. This unofficial port brings milkucha's Momentum addon forward to Minecraft Java 26.1.x on Fabric. Current builds are built and smoke-tested on Minecraft Java 26.1.2.

This addon is built for [Automobility: Unofficial Port](https://modrinth.com/mod/automobility-unofficial-port). Install Automobility first; Momentum does not include the base vehicle mod.

The goal is simple: make Automobility vehicles feel less abrupt and more controllable, especially when driving for more than a few seconds at a time. It adjusts acceleration, coasting, braking, steering, drift behavior, camera feel, and adds a compact speed HUD.

### Features

- Reworked acceleration and coasting behavior for smoother driving.
- Separate brake and handbrake/drift inputs.
- Multiple drift profiles and drift skid sounds.
- Camera handling tweaks for steering, braking, reversing, and drifting.
- Minimal speed HUD.
- Cruise control on the `C` key while driving, with HUD indication and target speed.
- Cruise control disengages when braking, drifting, reversing, leaving the driver seat, or hitting a wall or vehicle hard enough to count as an impact.
- Dedicated server key-state sync for brake and drift input.

### Requirements

- Minecraft Java 26.1.x
- Fabric Loader
- Fabric API
- [Automobility: Unofficial Port](https://modrinth.com/mod/automobility-unofficial-port) `0.5.0-unofficial.2` or newer

This addon is required on both the client and the server for multiplayer.

### Optional

YACL is optional. When installed, the Momentum options screen is available in-game.

### Compatibility

Compatibility testing has been done in singleplayer and on a multiplayer Fabric 26.1.2 server with a large mod set, including common performance mods, world generation mods, mapping mods, voice chat, controller support, shader/renderer mods, and quality-of-life mods. This does not guarantee compatibility with every modpack, so please include logs and a full mod list when reporting issues.

This project is not affiliated with or endorsed by the upstream Momentum maintainer unless stated otherwise by that maintainer. Bugs specific to this port should be reported to this fork.

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
