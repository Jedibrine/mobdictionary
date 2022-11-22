package ayamitsu.mobdictionary;

import net.minecraft.entity.player.*;
import java.io.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public abstract class AbstractProxy
{
    public abstract void preInit();
    
    public abstract void init();
    
    public abstract void postInit();
    
    public void displayScreen(final EntityPlayer player, final int gui) {
    }
    
    public boolean isDedicatedServer() {
        return false;
    }
    
    public EntityPlayer getPlayerInstance() {
        return null;
    }
    
    public File getSaveDirectory() {
        return null;
    }
    
    public MovingObjectPosition getMouseOver(final EntityLivingBase viewingEntity, final double reach) {
        return null;
    }
    
    public void printChatMessageClient(final IChatComponent chatComponent) {
    }
}
