# Changelog

## Momentum 0.1.5-unofficial.1 - 2026-06-26

Initial unofficial Fabric port of Momentum for Automobility to Minecraft Java 26.1.2.

### Added and Ported
- Ported Momentum's vehicle movement changes, braking, coasting, steering behavior, drift profiles, camera behavior, and minimal speed HUD to Minecraft 26.1.2.
- Ported client-to-server key state sync for brake and drift input on dedicated multiplayer servers.
- Ported the optional YACL config screen. It is available only when YACL is installed.

### Known Limits
- ModMenu integration is not included in this build because the available ModMenu API artifact for this environment is not compatible with the named Loom dependency setup used by this port.
- This addon requires Automobility `0.5.0-unofficial.2` or newer on both client and server.

## 0.5.0-unofficial.2 - 2026-06-26

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Fixed slope and steep slope block rendering on current Fabric so placed slopes render as sloped geometry instead of full cubes.
- Fixed the grass cutter front attachment so it harvests short grass, tall grass, ferns, dead bush, and flower-tagged blocks while leaving crops to the crop harvester.

## 0.5.0-unofficial.1 - 2026-06-20

Unofficial beta Fabric port of Automobility to Minecraft Java 26.1.2.

### Added and Ported
- Ported the Fabric build to Minecraft 26.1.2, Java 25, Fabric Loader 0.19.2, Fabric API 0.152.1+26.1.2, Gradle 9.6.0, and Loom 1.17.11.
- Restored vehicle rendering, item rendering, OBJ model loading, custom vehicle/resource-pack data, sounds, particles, and recipes on 26.1.2.
- Restored Auto Mechanic Table and Automobile Assembler workflows.
- Restored automobile frames, engines, wheels, front attachments, rear attachments, slopes, dash panels, off-road blocks, launch gel, autopilot sign, pressure plate, and crowbar behavior.
- Ported HUD speedometer placement, boost FOV, drift smoke, Controlify support, horn input, and vehicle audio.

### Fixed
- Fixed multiplayer visual displacement syncing so remote automobiles keep the correct rendered height and tilt when moving between elevations.
- Fixed inventory and held-item rendering paths for automobile parts and attachments on 26.1.2.

### Known Limits
- This is a Fabric-only beta release.
- Broader public modpack compatibility has not been exhaustively tested.
- This is an unofficial fork/port; upstream Automobility support channels should not be used for bugs introduced by this 26.1.2 port.
