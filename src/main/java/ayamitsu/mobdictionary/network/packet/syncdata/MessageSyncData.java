package ayamitsu.mobdictionary.network.packet.syncdata;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.*;
import cpw.mods.fml.common.network.*;

public class MessageSyncData implements IMessage
{
    String[] nameList;
    
    public MessageSyncData() {
        this.nameList = null;
    }
    
    public MessageSyncData(final String[] names) {
        this.nameList = null;
        this.nameList = names;
    }
    
    public void fromBytes(final ByteBuf buf) {
        final int length = buf.readInt();
        this.nameList = new String[length];
        for (int i = 0; i < length; ++i) {
            this.nameList[i] = ByteBufUtils.readUTF8String(buf);
        }
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.nameList.length);
        for (final String name : this.nameList) {
            ByteBufUtils.writeUTF8String(buf, name);
        }
    }
    
    public String[] getNameList() {
        return this.nameList;
    }
}
