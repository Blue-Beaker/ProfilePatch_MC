package io.bluebeaker.profilepatch.mixin_core;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import io.bluebeaker.profilepatch.ProfilePatchConfig;
import io.bluebeaker.profilepatch.SessionCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = YggdrasilMinecraftSessionService.class,remap = false)
public abstract class MixinMinecraftSessionService {
    // The first return when response==null but without an Exception
    // Can occur when the UUID is invalid
    @Inject(method = "fillGameProfile",at = @At(value = "RETURN",ordinal = 0))
    public void cacheNonExistentUUIDs(GameProfile profile, boolean requireSecure, CallbackInfoReturnable<GameProfile> cir){
        if(ProfilePatchConfig.expirationInterval>0) {
            SessionCache.getForService((MinecraftSessionService) this).putInvalidUUID(profile.getId());
        }
    }

    @Inject(method = "fillProfileProperties",at = @At("HEAD"),cancellable = true)
    public void skipNonExistentUUIDs(GameProfile profile, boolean requireSecure, CallbackInfoReturnable<GameProfile> cir){
        if(ProfilePatchConfig.expirationInterval>0 && SessionCache.getForService((MinecraftSessionService) this).isInvalid(profile.getId())) {
            cir.setReturnValue(profile);
        }
    }
}
