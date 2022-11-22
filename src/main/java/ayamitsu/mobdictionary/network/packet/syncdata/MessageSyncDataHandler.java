package ayamitsu.mobdictionary.network.packet.syncdata;

import cpw.mods.fml.common.network.simpleimpl.*;
import ayamitsu.mobdictionary.*;

public class MessageSyncDataHandler implements IMessageHandler<MessageSyncData, IMessage>
{
    public IMessage onMessage(final MessageSyncData message, final MessageContext ctx) {
        MobDatas.clearNameList();
        for (final String name : message.getNameList()) {
            MobDatas.addInfo((Object)name);
        }
        return null;
    }
}
