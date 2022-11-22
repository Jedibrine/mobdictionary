package ayamitsu.mobdictionary.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import ayamitsu.mobdictionary.network.packet.syncdata.*;
import cpw.mods.fml.relauncher.*;
import ayamitsu.mobdictionary.network.packet.register.*;
import cpw.mods.fml.common.network.*;

public class PacketHandler
{
    public static PacketHandler INSTANCE;
    public static final SimpleNetworkWrapper DISPATCHER;
    public static final int SYNC_DATA_ID = 0;
    public static final int REGISTER = 1;
    
    public void init() {
        PacketHandler.DISPATCHER.registerMessage((Class)MessageSyncDataHandler.class, (Class)MessageSyncData.class, 0, Side.CLIENT);
        PacketHandler.DISPATCHER.registerMessage((Class)MessageRegisterHandler.class, (Class)MessageRegister.class, 1, Side.SERVER);
    }
    
    static {
        PacketHandler.INSTANCE = new PacketHandler();
        DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel("AYA|MD");
    }
}
