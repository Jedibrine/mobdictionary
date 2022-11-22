package ayamitsu.mobdictionary.item;

import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import ayamitsu.mobdictionary.*;
import java.io.*;
import net.minecraft.util.*;
import net.minecraft.nbt.*;
import ayamitsu.mobdictionary.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.creativetab.*;
import java.util.*;

public class ItemMobData extends Item
{
    public ItemMobData() {
        this.setMaxStackSize(1);
    }
    
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final NBTTagCompound nbt = itemStackIn.getTagCompound();
        if (nbt != null) {
            final String name = getEntityNameFromNBT(nbt);
            if (MobDictionary.proxy.isDedicatedServer()) {
                if (playerIn instanceof EntityPlayerMP) {
                    final EntityPlayerMP playerMP = (EntityPlayerMP)playerIn;
                    if (!MobDatas.containsOnDedicatedServer(name, playerMP)) {
                        MobDatas.addInfoOnDedicatedServer((Object)name, playerMP);
                        try {
                            MobDatas.saveOnDedicatedServer(playerMP);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        --itemStackIn.stackSize;
                    }
                }
            }
            else if (!MobDatas.contains(name)) {
                MobDatas.addInfo((Object)name);
                if (worldIn.isRemote) {
                    ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ACCEPT, StatCollector.translateToLocal("entity." + name + ".name"));
                }
                try {
                    MobDatas.save();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                --itemStackIn.stackSize;
            }
            else if (worldIn.isRemote) {
                ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ALREADY, StatCollector.translateToLocal("entity." + name + ".name"));
            }
        }
        return itemStackIn;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        final NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            String name = getEntityNameFromNBT(nbt);
            if (name != null && name.length() > 0) {
                if (EntityUtils.isLivingName(name)) {
                    name = StatCollector.translateToLocal("entity." + name + ".name");
                }
                final StringBuilder sb = new StringBuilder().append(StatCollector.translateToLocal("mobdictionary.common.name")).append(":").append(name);
                tooltip.add(sb.toString());
            }
        }
    }
    
    public ItemStack getContainerItem(final ItemStack itemStack) {
        final ItemStack item = new ItemStack(itemStack.getItem());
        if (itemStack.hasTagCompound()) {
            item.setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
        }
        return item;
    }
    
    public boolean hasContainerItem(final ItemStack stack) {
        return stack.getItem() instanceof ItemMobData;
    }
    
    public static String getEntityNameFromNBT(final NBTTagCompound nbt) {
        return nbt.getString("Name");
    }
    
    public static void setEntityNameToNBT(final String name, final NBTTagCompound nbt) {
        nbt.setString("Name", name);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List subItems) {
    	final List<String> returnList = new ArrayList<String>();
    	returnList.addAll(EntityUtils.getStringToClassMapping().keySet());
        for (final String name : returnList) {
            if (EntityUtils.isLivingName(name)) {
                final ItemStack item = new ItemStack((Item)this);
                item.setTagCompound(new NBTTagCompound());
                setEntityNameToNBT(name, item.getTagCompound());
                subItems.add(item);
            }
        }
    }
}
