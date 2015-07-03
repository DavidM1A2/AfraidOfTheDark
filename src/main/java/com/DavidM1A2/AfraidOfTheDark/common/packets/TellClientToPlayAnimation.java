package com.DavidM1A2.AfraidOfTheDark.common.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;

public class TellClientToPlayAnimation implements IMessage
{
	private String animationName = "";
	private int entityIDToUpdate = 0;

	public TellClientToPlayAnimation()
	{
		this.animationName = "";
		this.entityIDToUpdate = 0;
	}

	public TellClientToPlayAnimation(String animationName, int entityIDToUpdate)
	{
		this.animationName = animationName;
		this.entityIDToUpdate = entityIDToUpdate;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.animationName = ByteBufUtils.readUTF8String(buf);
		this.entityIDToUpdate = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.animationName);
		buf.writeInt(this.entityIDToUpdate);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerClient implements IMessageHandler<TellClientToPlayAnimation, IMessage>
	{
		@Override
		public IMessage onMessage(final TellClientToPlayAnimation message, final MessageContext ctx)
		{
			Entity toUpdate = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityIDToUpdate);
			if (toUpdate != null)
			{
				if (toUpdate instanceof IMCAnimatedEntity)
				{
					((IMCAnimatedEntity) toUpdate).getAnimationHandler().activateAnimation(message.animationName, 0);
				}
			}
			return null;
		}
	}
}