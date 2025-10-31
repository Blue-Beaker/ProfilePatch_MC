package io.bluebeaker.profilepatch.mixin_core;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import io.bluebeaker.profilepatch.SessionCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = YggdrasilMinecraftSessionService.class,remap = false)
public abstract class MixinMinecraftSessionService {

    @Inject(method = "fillGameProfile",at = @At(value = "RETURN",ordinal = 0))
    public void cacheNonExistentUUIDs(GameProfile profile, boolean requireSecure, CallbackInfoReturnable<GameProfile> cir){
        SessionCache.getForService((MinecraftSessionService) this).putInvalidUUID(profile.getId());
    }

    @Inject(method = "fillProfileProperties",at = @At("HEAD"),cancellable = true)
    public void skipNonExistentUUIDs(GameProfile profile, boolean requireSecure, CallbackInfoReturnable<GameProfile> cir){
        if(SessionCache.getForService((MinecraftSessionService) this).isInvalid(profile.getId())) {
            cir.setReturnValue(profile);
        }
    }
}
