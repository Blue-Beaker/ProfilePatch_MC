package io.bluebeaker.threadedskinloading;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

public class ThreadedSkinLoadingMixins implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.threadedskinloading.json");
    }
}
