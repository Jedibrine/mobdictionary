package ayamitsu.mobdictionary;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import ayamitsu.mobdictionary.item.*;
import ayamitsu.mobdictionary.item.ItemMobDictionary.EnumChatMessage;
import cpw.mods.fml.common.registry.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import ayamitsu.mobdictionary.item.crafting.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.oredict.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.*;

import java.io.File;

import ayamitsu.mobdictionary.event.*;
import ayamitsu.mobdictionary.network.*;
import ayamitsu.mobdictionary.network.packet.register.MessageRegister;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@Mod(modid = "mobdictionary", name = "MobDictionary", version = "1.0")
public class MobDictionary
{
    public static final String MODID = "mobdictionary";
    public static final String NAME = "MobDictionary";
    public static final String VERSION = "1.0";
    public static final String CHANNEL = "AYA|MD";
    public static final int GUI_DICTIONARY = 0;
    @Mod.Instance("mobdictionary")
    public static MobDictionary instance;
    @SidedProxy(clientSide = "ayamitsu.mobdictionary.client.ClientProxy", serverSide = "ayamitsu.mobdictionary.server.ServerProxy")
    public static AbstractProxy proxy;
    public static CreativeTabs tabMobDictionary;
    public static Item mobDictionary;
    public static Item mobData;
	public static String blacklistedEntities;
	
	public static void initConfiguration(FMLInitializationEvent event) {
    	Configuration config = new Configuration(new File("config/mobdictionary.cfg"));
    	config.load();
    	blacklistedEntities = config.getString("blacklistedEntities", "config", "", "Blacklisted entities list, list entitity names seperated by commas");
    	config.save();
    }
	
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        MobDictionary.mobDictionary = new ItemMobDictionary().setUnlocalizedName("mobdictionary.dictionary").setTextureName("mobdictionary:dictionary");
        MobDictionary.mobData = new ItemMobData().setCreativeTab(MobDictionary.tabMobDictionary).setUnlocalizedName("mobdictionary.data").setTextureName("mobdictionary:data");
        MobDictionary.tabMobDictionary = new CreativeTabs("mobdictionary") {
            public Item getTabIconItem() {
                return MobDictionary.mobDictionary;
            }
        };
        MobDictionary.mobDictionary.setCreativeTab(MobDictionary.tabMobDictionary);
        MobDictionary.mobData.setCreativeTab(MobDictionary.tabMobDictionary);
        GameRegistry.registerItem(MobDictionary.mobDictionary, "dictionary");
        GameRegistry.registerItem(MobDictionary.mobData, "data");
        GameRegistry.addShapelessRecipe(new ItemStack(MobDictionary.mobDictionary, 1), new Object[] { new ItemStack(Items.book, 1), new ItemStack(Blocks.sapling, 1, 0), new ItemStack(Blocks.sapling, 1, 1), new ItemStack(Blocks.sapling, 1, 2), new ItemStack(Blocks.sapling, 1, 3) });
        RecipeSorter.register("mobdictionary:data", (Class)RecipeMobData.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        GameRegistry.addRecipe((IRecipe)new RecipeMobData());
        FMLCommonHandler.instance().bus().register((Object)new PlayerLoggedInHandler());
        PacketHandler.INSTANCE.init();
        MobDictionary.proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MobDictionary.proxy.init();
        MobDictionary.initConfiguration(event);
        MinecraftForge.EVENT_BUS.register(KillChecker.instance);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        MobDatas.initAllMobValue();
        MobDictionary.proxy.postInit();
    }
}
