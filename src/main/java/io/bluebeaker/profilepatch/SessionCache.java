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
        // Put the expiration timestamp
        this.cachedInvalidUUIDs.put(uuid,System.currentTimeMillis()+expirationInterval* 1000L);
        if(ProfilePatchConfig.logUUIDs){
            ProfilePatch.getLogger().info("Added non-existent player UUID {} to cache.",uuid);
        }
    }
    public boolean isInvalid(UUID uuid){
        // Check expiration
        Long l = this.cachedInvalidUUIDs.get(uuid);
        return l!=null && System.currentTimeMillis() < l;
    }
    public void clean(){
        long time = System.currentTimeMillis();
        for (UUID uuid : new ArrayList<>(cachedInvalidUUIDs.keySet())) {
            // Clean expired entries
            if(time > this.cachedInvalidUUIDs.get(uuid)){
                cachedInvalidUUIDs.remove(uuid);
            }
        }
    }
    public static void cleanAll(){
        instances.values().forEach(SessionCache::clean);
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
