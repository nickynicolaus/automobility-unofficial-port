package io.github.foundationgames.automobility.util;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class AutomobilityClientResourceDumper {
    public static final Path DUMP_DIR = FabricLoader.getInstance().getGameDir().resolve("automobility_dump");
}
