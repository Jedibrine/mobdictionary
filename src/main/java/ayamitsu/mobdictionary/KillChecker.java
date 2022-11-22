package ayamitsu.mobdictionary;

import ayamitsu.mobdictionary.item.ItemMobData;
import ayamitsu.mobdictionary.item.ItemMobDictionary;
import ayamitsu.mobdictionary.network.PacketHandler;
import ayamitsu.mobdictionary.network.packet.register.MessageRegister;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class KillChecker {

	public static KillChecker instance = new KillChecker();
	
	@SubscribeEvent
    public void onKill(LivingDeathEvent event) {
        if (Minecraft.getMinecraft().thePlayer!=null && Minecraft.getMinecraft().theWorld!=null) {
        	if (event.source!=null) {
		    	if (event.source.getEntity() instanceof EntityPlayerMP) {
		    		if (event.source.getEntity().getEntityId() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
				    	final EntityPlayer playerIn = (EntityPlayer)event.source.getEntity();
				    	if (event.entityLiving!=null && !MobDictionary.blacklistedEntities.contains(event.entityLiving.getCommandSenderName())) {
				    		final EntityLivingBase living = event.entityLiving;
				            final Class clazz = living.getClass();
				            if (MobDatas.contains(clazz)) {
				                if (ItemMobDictionary.hasItemPlayer(playerIn, Items.paper)) {
				                    if (ItemMobDictionary.hasEmptyPlayer(playerIn)) {
				                    	ItemMobDictionary.consumeItemPlayer(playerIn, Items.paper);
				                        final ItemStack is = new ItemStack(MobDictionary.mobData);
				                        final NBTTagCompound nbt = new NBTTagCompound();
				                        ItemMobData.setEntityNameToNBT(EntityList.getEntityString((Entity)living), nbt);
				                        is.setTagCompound(nbt);
				                        ItemMobDictionary.addItemStackPlayer(playerIn, is);
				                    }
				                    else {
				                    	ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ALREADY, StatCollector.translateToLocal("entity." + EntityList.getEntityString((Entity)living) + ".name"));
				                    }
				                }
				                else {
				                	ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ALREADY, StatCollector.translateToLocal("entity." + EntityList.getEntityString((Entity)living) + ".name"));
				                }
				            }
				            else {
				                MobDatas.addInfo((Object)clazz);
				                ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ACCEPT, StatCollector.translateToLocal("entity." + EntityList.getEntityString((Entity)living) + ".name"));
				                PacketHandler.DISPATCHER.sendToServer((IMessage)new MessageRegister(playerIn, living));
				            }
				        }
			    	}
		    	}
        	}
        }
    }
}
