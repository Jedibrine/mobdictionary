package ayamitsu.mobdictionary.server;

import java.io.File;

import ayamitsu.mobdictionary.AbstractProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.server.MinecraftServer;

@SideOnly(Side.SERVER)
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
