# Changelog

## 0.5.0-unofficial.23 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Included the automobile's authoritative server position in the custom sync packet and snapped stopped client-side automobiles to that position before recalculating auxiliary hitboxes. This prevents parked vehicles that coasted after a dismount from keeping a stale rear hitbox until the next small physical movement.

## 0.5.0-unofficial.22 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Kept automobile hitbox collision active for recently dismounted players, but now continuously resolves overlap while the vehicle is settling after an unmanned coast. This prevents players from entering the vehicle hitbox during the grace window and getting stuck when collision resumes.

## 0.5.0-unofficial.21 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Kept the recent-dismount collision grace active for the full unmanned coasting window plus a short settle period, preventing the former driver from getting stuck on the automobile hitbox after jumping out while the vehicle rolls to a stop.

## 0.5.0-unofficial.20 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Stopped recreating auxiliary hitbox entities every time an unmanned automobile finishes coasting; the settle path now validates and repositions the existing hitboxes like a normally stopped vehicle.

## 0.5.0-unofficial.19 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Moved recently dismounted players out of current auxiliary automobile hitboxes after dismount stabilization and after unmanned coasting settles, reducing edge jitter when the vehicle stops near the player.

## 0.5.0-unofficial.18 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Pruned stale duplicate auxiliary hitboxes on the client after server-side hitbox recreation so local player collision uses the current hitbox set.

## 0.5.0-unofficial.17 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Removed direct living-entity collision from the main automobile entity so players collide with the detailed auxiliary hitboxes instead of two overlapping vehicle collision layers.
- Restored normal auxiliary hitbox collision for recently dismounted players after the previous test build made overlap jitter worse.

## 0.5.0-unofficial.16 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Kept recent-dismount collision grace active while an unmanned automobile coasts to a stop.
- Ignored automobile and auxiliary hitbox collision against the player who just dismounted, reducing camera jitter when walking into the vehicle immediately after a moving dismount.
- Removed orphaned auxiliary hitbox entities on the server if they are no longer tracked by their automobile.

## 0.5.0-unofficial.15 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Made automobile and auxiliary hitbox entity collision checks symmetric.
- Rebuilt auxiliary hitboxes after an unmanned automobile finishes coasting, preventing stale collision areas from lingering on some vehicle parts.

## 0.5.0-unofficial.14 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Changed
- Moved the in-vehicle control hints to the upper-right HUD area by default.

## Momentum 0.1.11-unofficial.3 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port of Momentum for Automobility.

### Fixed
- Prevented the drift key from pushing automobiles into reverse when held at low speed; reverse remains on the brake key.

## 0.5.0-unofficial.13 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Settled unmanned automobiles once they finish coasting after a moving dismount, clearing residual physics and resynchronizing hitboxes.

## 0.5.0-unofficial.12 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Restored physical collision for detailed automobile hitbox entities so rear vehicle sections cannot be walked through while the automobile coasts after dismount.
- Restored Automobility control hints when Momentum's HUD is enabled.

## 0.5.0-unofficial.11 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Changed
- Allowed automobiles to coast after the driver dismounts while still clearing driver input and stabilizing hitbox synchronization.
- Stopped active boost on driver dismount so unmanned automobiles coast from their current engine speed instead of continuing powered boost.

## 0.5.0-unofficial.10 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Prevented moving dismount fallback from placing the player at the center of the automobile.
- Tried both sides of the automobile when choosing a dismount location and increased side clearance.

## 0.5.0-unofficial.9 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Forced vehicle interpolation and hitbox positions to settle immediately when the driver dismounts while moving.
- Restored normal walking collision immediately after dismount while keeping the recent-dismount protection against run-over knockback.
- Gave the motorcar windshield/dashboard planes a tiny depth to avoid zero-thickness face flicker during engine shake.

## 0.5.0-unofficial.8 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Rendered only the transparent upper windshield pixels through the motorcar glass pass.
- Moved the opaque lower windshield/dashboard pixels back into the normal motorcar body pass to reduce flicker while the engine shake animation is active.

## 0.5.0-unofficial.7 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Restored the full motorcar windshield height after the previous visual test build left a gap.
- Moved the separate motorcar windshield plane away from nearby body geometry to reduce flickering under the windshield.

## 0.5.0-unofficial.6 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Stabilized vehicles immediately when the driver dismounts, clearing residual speed and collision velocity left by jumping out while moving.
- Split motorcar glass into its own translucent render pass so the body can render separately from the windshield.

## 0.5.0-unofficial.5 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Restored the motorcar windshield plane position and instead offset the body face behind it to target dashboard-area z-fighting.
- Prevented detailed automobile hitbox entities from acting as direct walking collision for living entities; player collision now uses the stable main automobile entity instead.

## 0.5.0-unofficial.4 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Fixed
- Offset the motorcar windshield plane slightly away from the front body face to reduce dashboard-area z-fighting.
- Kept automobile hitbox entities on stable chassis coordinates instead of visual suspension displacement to reduce player collision jitter after driving.
- Added a short dismount grace period so the former driver is not immediately pushed or run over by the same vehicle.

## 0.5.0-unofficial.3 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port.

### Changed
- Changed motorcar frame rendering away from full-model translucent rendering to reduce windshield shimmer.
- Made automobile hitbox collision active consistently on client and server to reduce player clipping and jitter after driving.

## Momentum 0.1.11-unofficial.1 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port of Momentum for Automobility.

### Changed
- Reduced the cruise control HUD chevron size.
- Made the cruise control HUD color stable while active instead of switching with throttle/coast state.

## Momentum 0.1.10-unofficial.1 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port of Momentum for Automobility.

### Changed
- Replaced the cruise control HUD icon with compact procedural chevrons that match the existing speed HUD.

## Momentum 0.1.9-unofficial.1 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port of Momentum for Automobility.

### Changed
- Increased the cruise control HUD icon size and added a procedural fallback so the cruise indicator remains visible if texture rendering fails in the HUD layer.

## Momentum 0.1.8-unofficial.1 - 2026-06-27

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port of Momentum for Automobility.

### Changed
- Replaced the procedural cruise control HUD glyph with a dedicated texture icon.

## Momentum 0.1.7-unofficial.1 - 2026-06-26

Patch release for the unofficial Minecraft Java 26.1.2 Fabric port of Momentum for Automobility.

### Added
- Added cruise control on the `C` key while driving an Automobility vehicle.
- Added cruise control HUD indication to the existing Momentum speed HUD, including target speed and active/coasting color changes.

### Fixed
- Cruise control now disengages when braking, drifting, reversing, leaving the driver seat, or hitting a wall/vehicle hard enough to count as an impact.
- Cruise throttle now avoids direct speed injection and instead uses normal Automobility forward input so multiplayer behavior stays compatible with the existing vehicle sync.

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
