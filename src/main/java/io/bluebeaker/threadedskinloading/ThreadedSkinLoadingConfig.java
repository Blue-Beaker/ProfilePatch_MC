package io.bluebeaker.threadedskinloading;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = ThreadedSkinLoading.MODID,type = Type.INSTANCE,category = "general")
public class ThreadedSkinLoadingConfig {
    @Comment("Example")
    @LangKey("config.threadedskinloading.example.name")
    public static boolean example = true;
}