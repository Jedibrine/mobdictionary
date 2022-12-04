package ayamitsu.mobdictionary;

import java.io.File;

import ayamitsu.mobdictionary.event.ConfigChangedHandler;
import ayamitsu.mobdictionary.event.KillChecker;
import ayamitsu.mobdictionary.event.PlayerLoggedInHandler;
import ayamitsu.mobdictionary.item.ItemMobData;
import ayamitsu.mobdictionary.item.ItemMobDictionary;
import ayamitsu.mobdictionary.item.crafting.RecipeMobData;
import ayamitsu.mobdictionary.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.RecipeSorter;

@Mod(modid = "mobdictionary", name = "MobDictionary", version = "1.4", acceptedMinecraftVersions = "[1.7.10]", guiFactory = "ayamitsu.mobdictionary.client.gui.MDGuiFactory")
public class MobDictionary
{
    public static final String MODID = "mobdictionary";
    public static final String NAME = "MobDictionary";
    public static final String VERSION = "1.4";
    public static final String CHANNEL = "AYA|MD";
    public static final int GUI_DICTIONARY = 0;
    @Mod.Instance("mobdictionary")
    public static MobDictionary instance;
    @SidedProxy(clientSide = "ayamitsu.mobdictionary.client.ClientProxy", serverSide = "ayamitsu.mobdictionary.server.ServerProxy")
    public static AbstractProxy proxy;
    public static CreativeTabs tabMobDictionary;
    public static Item mobDictionary;
    public static Item mobData;
    
    public static Configuration config = new Configuration(new File("config/mobdictionary.cfg"));
    Property blacklistedEntitiesp = config.get(Configuration.CATEGORY_GENERAL, "blacklistedEntities", "", "Blacklisted entities list, list entitity names seperated by commas");
    Property maxNumberp = config.get(Configuration.CATEGORY_GENERAL, "maxNumber", 0, "Maximum value displayed in the mob dictionary. 0 makes it use the default amount of entities");
	public static String blacklistedEntities;
	public static int maxNumber;
	
	
	public static void load (Configuration config) {
		Property blacklistedEntitiesp = config.get(Configuration.CATEGORY_GENERAL, "blacklistedEntities", "", "Blacklisted entities list, list entitity names seperated by commas");
	    Property maxNumberp = config.get(Configuration.CATEGORY_GENERAL, "maxNumber", 0, "Maximum value displayed in the mob dictionary. 0 makes it use the default amount of entities");
		blacklistedEntities = blacklistedEntitiesp.getString();
		maxNumber = maxNumberp.getInt();
		if (config.hasChanged()) {
            config.save();
		}
	}
	
	public static void setupAndLoad(FMLPreInitializationEvent event) {
    	config.load();
    	MobDictionary.load(config);
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
        MobDictionary.setupAndLoad(event);
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register((Object)new ConfigChangedHandler());
		MobDictionary.proxy.init();
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        MobDatas.initAllMobValue();
        MobDictionary.proxy.postInit();
    }
}
