package ayamitsu.mobdictionary.client.gui;

import java.util.ArrayList;
import java.util.List;

import ayamitsu.mobdictionary.MobDictionary;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class MDGuiConfig extends GuiConfig {
	
	public MDGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, 
				new ConfigElement(MobDictionary.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), MobDictionary.MODID, 
				false, false, MobDictionary.MODID);
	}
	
}