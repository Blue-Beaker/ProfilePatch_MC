package io.bluebeaker.profilepatch;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Logger;

import io.bluebeaker.profilepatch.Tags;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import static io.bluebeaker.profilepatch.ProfilePatchConfig.cleanInterval;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,acceptableRemoteVersions = "*")
public class ProfilePatch
{
    public static final String MODID = Tags.MOD_ID;
    public static final String NAME = Tags.MOD_NAME;
    public static final String VERSION = Tags.VERSION;
    
    public MinecraftServer server;

    private static Logger logger;

    private static Timer cleanTimer;
    
    public ProfilePatch() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }
    @EventHandler
    public void onServerStart(FMLServerStartingEvent event){
        this.server=event.getServer();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        cleanTimer = new Timer();
        // Clean every hour
        cleanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SessionCache.cleanAll();
            }
        }, cleanInterval* 1000L, cleanInterval* 1000L);
    }

    @SubscribeEvent
    public void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Type.INSTANCE);
        }
    }

    public static Logger getLogger(){
        return logger;
    }
}
