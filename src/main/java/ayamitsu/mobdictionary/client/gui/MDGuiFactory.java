package ayamitsu.mobdictionary.client.gui;

import java.util.Set;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class MDGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft mc) {
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return MDGuiConfig.class;
	}
	
	@Override
	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
	
}
