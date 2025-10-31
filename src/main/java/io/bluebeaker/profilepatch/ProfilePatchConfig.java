package io.bluebeaker.profilepatch;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = ProfilePatch.MODID,type = Type.INSTANCE,category = "general")
public class ProfilePatchConfig {
    @Comment("Log cached invalid UUIDs")
    public static boolean logUUIDs = false;

    @Comment("Expiration time for cached invalid UUIDs, in seconds")
    public static int expirationInterval = 21600;

    @Config.RequiresMcRestart
    @Comment("Time interval to clean cached invalid UUIDs, in seconds")
    public static int cleanInterval = 3600;
}