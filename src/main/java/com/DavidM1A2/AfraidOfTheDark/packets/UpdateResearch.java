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
	private boolean isCompleted;

	public UpdateResearch()
	{
		this.index = -1;
		this.isCompleted = false;
	}

	public UpdateResearch(int index, boolean isCompleted)
	{
		this.index = index;
		this.isCompleted = isCompleted;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.index = buf.readInt();
		this.isCompleted = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(index);
		buf.writeBoolean(isCompleted);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerServer implements IMessageHandler<UpdateResearch, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateResearch message, MessageContext ctx)
		{
			LogHelper.info("Update Research #" + message.index + " received! Status: " + message.isCompleted);
			LoadResearchData data = LoadResearchData.get(ctx.getServerHandler().playerEntity);
			data.setResearch(message.index, message.isCompleted);
			return null;
		}
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerClient implements IMessageHandler<UpdateResearch, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateResearch message, MessageContext ctx)
		{
			LogHelper.info("Update Research #" + message.index + " received! Status: " + message.isCompleted);
			LoadResearchData data = LoadResearchData.get(Minecraft.getMinecraft().thePlayer);
			data.setResearch(message.index, message.isCompleted);
			return null;
		}
	}
}
