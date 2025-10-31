package io.bluebeaker.profilepatch;

import com.mojang.authlib.minecraft.MinecraftSessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;

import static io.bluebeaker.profilepatch.ProfilePatchConfig.expirationInterval;

public class SessionCache {
    private static final HashMap<MinecraftSessionService,SessionCache> instances = new HashMap<>();
    private final HashMap<UUID,Long> cachedInvalidUUIDs = new HashMap<>();

    public static SessionCache getForService(MinecraftSessionService sessionService){
        SessionCache inst = instances.get(sessionService);
        if(inst==null){
            inst=new SessionCache();
            instances.put(sessionService,inst);
        }
        return inst;
    }

    public void putInvalidUUID(UUID uuid){
        this.cachedInvalidUUIDs.put(uuid,System.currentTimeMillis());
        if(ProfilePatchConfig.logUUIDs){
            ProfilePatch.getLogger().info("Added non-existent player UUID {} to cache.",uuid);
        }
    }
    public boolean isInvalid(UUID uuid){
        // Only cache for 6 hours
        Long l = this.cachedInvalidUUIDs.get(uuid);
        return l!=null && System.currentTimeMillis() < l +expirationInterval* 1000L;
    }
    public void clean(){
        long time = System.currentTimeMillis();
        for (UUID uuid : new ArrayList<>(cachedInvalidUUIDs.keySet())) {
            if(time > this.cachedInvalidUUIDs.get(uuid) +expirationInterval* 1000L){
                cachedInvalidUUIDs.remove(uuid);
            }
        }
    }
    public static void cleanAll(){
        for (SessionCache inst : instances.values()) {
            inst.clean();
        }
    }

    public static TimerTask getCleanTask(){
        return new TimerTask() {
            @Override
            public void run() {
                cleanAll();
            }
        };
    }
}
