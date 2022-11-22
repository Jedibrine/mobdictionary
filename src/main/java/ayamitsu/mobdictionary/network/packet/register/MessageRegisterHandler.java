package ayamitsu.mobdictionary.network.packet.register;

import cpw.mods.fml.common.network.simpleimpl.*;
import ayamitsu.mobdictionary.*;
import java.io.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;

public class MessageRegisterHandler implements IMessageHandler<MessageRegister, IMessage>
{
    public IMessage onMessage(final MessageRegister message, final MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final Entity entity = player.worldObj.getEntityByID(message.getEntityId());
        if (player.getUniqueID().toString().equals(message.getUUIDString()) && entity instanceof EntityLivingBase) {
            final EntityLivingBase target = (EntityLivingBase)entity;
            if (MobDictionary.proxy.isDedicatedServer()) {
                MobDatas.addInfoOnDedicatedServer((Object)target.getClass(), player);
                try {
                    MobDatas.saveOnDedicatedServer(player);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                MobDatas.addInfo((Object)target.getClass());
                try {
                    MobDatas.save();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
