package ayamitsu.mobdictionary.event;

import ayamitsu.mobdictionary.MobDictionary;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigChangedHandler {
	
	@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    	if ("mobdictionary".equals(event.modID)){
			MobDictionary.load(MobDictionary.config);
    	}
	}

}