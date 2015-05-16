package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class UpdateResearch implements IMessage
{
	private int index;
	private boolean unlocked;

	public UpdateResearch()
	{
		index = -1;
		unlocked = false;
	}

	public UpdateResearch(int index, boolean unlocked)
	{
		this.index = index;
		this.unlocked = unlocked;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.index = buf.readInt();
		this.unlocked = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(index);
		buf.writeBoolean(unlocked);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerServer implements IMessageHandler<UpdateResearch, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateResearch message, MessageContext ctx)
		{
			LogHelper.info("Update Server Research: index = " + message.index + " value = " + message.unlocked);
			LoadResearchData.setSingleResearch(ctx.getServerHandler().playerEntity, message.index, message.unlocked);
			return null;
		}
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerClient implements IMessageHandler<UpdateResearch, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateResearch message, MessageContext ctx)
		{
			LogHelper.info("Update Client Research: index = " + message.index + " value = " + message.unlocked);
			LoadResearchData.setSingleResearch(Minecraft.getMinecraft().thePlayer, message.index, message.unlocked);
			return null;
		}
	}
}
