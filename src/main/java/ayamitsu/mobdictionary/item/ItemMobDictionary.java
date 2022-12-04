package ayamitsu.mobdictionary.item;

import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import ayamitsu.mobdictionary.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import ayamitsu.mobdictionary.network.*;
import ayamitsu.mobdictionary.network.packet.register.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import java.util.*;

public class ItemMobDictionary extends Item
{
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (worldIn.isRemote) {
        	this.displayDictionary(playerIn);
        }
        return itemStackIn;
    }
    
    @SideOnly(Side.CLIENT)
    public void displayDictionary(final EntityPlayer player) {
        MobDictionary.proxy.displayScreen(player, 0);
    }
    
    public static boolean hasItemPlayer(final EntityPlayer player, final Item item) {
        final InventoryPlayer inventory = player.inventory;
        return inventory.hasItem(item);
    }
    
    public static boolean hasEmptyPlayer(final EntityPlayer player) {
        final InventoryPlayer inventory = player.inventory;
        return inventory.getFirstEmptyStack() > 0;
    }
    
    public static void consumeItemPlayer(final EntityPlayer player, final Item item) {
        final InventoryPlayer inventory = player.inventory;
        inventory.consumeInventoryItem(item);
    }
    
    public static void addItemStackPlayer(final EntityPlayer player, final ItemStack itemStack) {
        final InventoryPlayer inventory = player.inventory;
        inventory.addItemStackToInventory(itemStack);
    }
    
    public static void showChatMessage(final EnumChatMessage chatMessage, final Object... objects) {
        final IChatComponent chatComponent = (IChatComponent)new ChatComponentText(chatMessage.getTranslatedText(objects));
        MobDictionary.proxy.printChatMessageClient(chatComponent);
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
    	int limit = MobDatas.getAllMobValue();
    	int blacklistlength = 1;
        char[] blacklist = MobDictionary.blacklistedEntities.toCharArray();
        char comma = ',';
        for (int i=0; i<blacklist.length; i++) {
        	if (blacklist[i]==comma) {
        		blacklistlength++;
        	}
        }
    	if (MobDictionary.maxNumber>0) {
    		limit = MobDictionary.maxNumber;
    	}
    	else if (MobDictionary.maxNumber==0) {
    		limit = MobDatas.getAllMobValue();
    	}
    	else {
    		limit = MobDatas.getAllMobValue()-blacklistlength;
    	}
		StringBuilder sb = new StringBuilder(StatCollector.translateToLocal("mobdictionary.common.registered_value")).append(": ").append(MobDatas.getRegisteredValue()).append('/').append(limit);
    	tooltip.add(sb.toString());
    }
    
    public enum EnumChatMessage
    {
        ACCEPT("mobdictionary.common.register_accept"), 
        ALREADY("mobdictionary.common.register_already"), 
        ERROR("mobdictionary.common.register_error");
        
        String text;
        
        private EnumChatMessage(final String text) {
            this.text = text;
        }
        
        public String getText() {
            return this.text;
        }
        
        public String getTranslatedText(final Object... objects) {
            return StatCollector.translateToLocalFormatted(this.text, objects);
        }
    }
}
