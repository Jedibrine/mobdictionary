package ayamitsu.mobdictionary.server;

import ayamitsu.mobdictionary.*;
import net.minecraft.server.*;
import java.io.*;
import cpw.mods.fml.server.*;
import cpw.mods.fml.common.*;

public class ServerProxy extends AbstractProxy
{
    public void preInit() {
    }
    
    public void init() {
    }
    
    public void postInit() {
    }
    
    public boolean isDedicatedServer() {
        return MinecraftServer.getServer().isDedicatedServer();
    }
    
    public File getSaveDirectory() {
        return this.isDedicatedServer() ? new File(FMLServerHandler.instance().getSavesDirectory(), "world/playerdata/mobdictionary").getAbsoluteFile() : new File(Loader.instance().getConfigDir(), "/dictionary").getAbsoluteFile();
    }
}
