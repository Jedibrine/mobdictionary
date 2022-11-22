//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedib\Downloads\Minecraft-Deobfuscator3000-1.2.3\1.7.10 stable mappings"!

//Decompiled by Procyon!

package ayamitsu.mobdictionary.event;

import cpw.mods.fml.common.gameevent.*;
import net.minecraft.entity.player.*;
import ayamitsu.mobdictionary.*;
import java.io.*;
import ayamitsu.mobdictionary.network.packet.syncdata.*;
import ayamitsu.mobdictionary.network.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import java.util.*;
import cpw.mods.fml.common.eventhandler.*;

public class PlayerLoggedInHandler
{
    @SubscribeEvent
    public void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            final EntityPlayerMP player = (EntityPlayerMP)event.player;
            if (MobDictionary.proxy.isDedicatedServer()) {
                try {
                    MobDatas.loadOnDedicatedServer(player);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                final UUID uuid = player.getUniqueID();
                final String[] nameList = MobDatas.toArrayOnDedicatedServer(player);
                final MessageSyncData msg = new MessageSyncData(nameList);
                PacketHandler.DISPATCHER.sendTo((IMessage)msg, player);
            }
            else {
                try {
                    MobDatas.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                final String[] nameList2 = MobDatas.toArray();
                final MessageSyncData msg2 = new MessageSyncData(nameList2);
                PacketHandler.DISPATCHER.sendTo((IMessage)msg2, player);
            }
        }
    }
}
