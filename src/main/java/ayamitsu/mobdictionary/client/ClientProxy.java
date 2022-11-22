//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedib\Downloads\Minecraft-Deobfuscator3000-1.2.3\1.7.10 stable mappings"!

//Decompiled by Procyon!

package ayamitsu.mobdictionary.client;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import ayamitsu.mobdictionary.AbstractProxy;
import ayamitsu.mobdictionary.client.gui.GuiMobDictionary;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class ClientProxy extends AbstractProxy
{
    private static HashMap<Integer, Class<? extends GuiScreen>> guiMap;
    private static Minecraft mc;
    
    public void preInit() {
        ClientProxy.guiMap.put(0, GuiMobDictionary.class);
    }
    
    public void init() {
    }
    
    public void postInit() {
    }
    
    public void displayScreen(final EntityPlayer player, final int guiID) {
        GuiScreen screen = null;
        try {
            final Class clazz = ClientProxy.guiMap.get(guiID);
            screen = (GuiScreen) clazz.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (screen != null) {
            FMLClientHandler.instance().displayGuiScreen(player, screen);
        }
    }
    
    public EntityPlayer getPlayerInstance() {
        return (EntityPlayer)FMLClientHandler.instance().getClientPlayerEntity();
    }
    
    public File getSaveDirectory() {
        return new File(Loader.instance().getConfigDir(), "/dictionary").getAbsoluteFile();
    }
    
    public MovingObjectPosition getMouseOver(final EntityLivingBase viewingEntity, final double reach) {
        final Timer timer = ((Timer)ObfuscationReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)ClientProxy.mc, 16));
        final float renderPartialTicks = timer.renderPartialTicks;
        MovingObjectPosition mop = null;
        if (viewingEntity != null && viewingEntity.worldObj != null) {
            mop = viewingEntity.rayTrace(reach, renderPartialTicks);
            final Vec3 viewPosition = viewingEntity.getPosition(renderPartialTicks);
            double d1 = 0.0;
            if (mop != null) {
                d1 = mop.hitVec.distanceTo(viewPosition);
            }
            final Vec3 lookVector = viewingEntity.getLook(renderPartialTicks);
            final Vec3 reachVector = viewPosition.addVector(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach);
            Vec3 vec33 = null;
            final float f1 = 1.0f;
            final List<Entity> list = (List<Entity>)viewingEntity.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)viewingEntity, viewingEntity.boundingBox.addCoord(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach).expand((double)f1, (double)f1, (double)f1));
            double d2 = d1;
            Entity pointedEntity = null;
            for (final Entity entity : list) {
                if (entity.canBeCollidedWith()) {
                    final float collisionSize = entity.getCollisionBorderSize();
                    final AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)collisionSize, (double)collisionSize, (double)collisionSize);
                    final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(viewPosition, reachVector);
                    if (axisalignedbb.isVecInside(viewPosition)) {
                        if (0.0 >= d2 && d2 != 0.0) {
                            continue;
                        }
                        pointedEntity = entity;
                        vec33 = ((movingobjectposition == null) ? viewPosition : movingobjectposition.hitVec);
                        d2 = 0.0;
                    }
                    else {
                        if (movingobjectposition == null) {
                            continue;
                        }
                        final double d3 = viewPosition.distanceTo(movingobjectposition.hitVec);
                        if (d3 >= d2 && d2 != 0.0) {
                            continue;
                        }
                        if (entity == viewingEntity.ridingEntity && !entity.canRiderInteract()) {
                            if (d2 != 0.0) {
                                continue;
                            }
                            pointedEntity = entity;
                            vec33 = movingobjectposition.hitVec;
                        }
                        else {
                            pointedEntity = entity;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
            if (pointedEntity != null && (d2 < d1 || mop == null)) {
                mop = new MovingObjectPosition(pointedEntity, vec33);
            }
        }
        return mop;
    }
    
    public void printChatMessageClient(final IChatComponent chatComponent) {
        ClientProxy.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }
    
    static {
        ClientProxy.guiMap = new HashMap<Integer, Class<? extends GuiScreen>>();
        ClientProxy.mc = Minecraft.getMinecraft();
    }
}
