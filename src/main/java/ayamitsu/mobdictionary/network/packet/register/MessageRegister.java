package ayamitsu.mobdictionary.network.packet.register;

import cpw.mods.fml.common.network.simpleimpl.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import io.netty.buffer.*;
import cpw.mods.fml.common.network.*;

public class MessageRegister implements IMessage
{
    private String uuidString;
    private int entityId;
    
    public MessageRegister() {
    }
    
    public MessageRegister(final EntityPlayer player, final EntityLivingBase living) {
        this.uuidString = player.getUniqueID().toString();
        this.entityId = living.getEntityId();
    }
    
    public String getUUIDString() {
        return this.uuidString;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.uuidString = ByteBufUtils.readUTF8String(buf);
        this.entityId = buf.readInt();
    }
    
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuidString);
        buf.writeInt(this.entityId);
    }
}
