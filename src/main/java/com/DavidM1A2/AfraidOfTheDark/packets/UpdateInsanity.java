package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class UpdateInsanity implements IMessage
{
	private double insanity;

	public UpdateInsanity()
	{
		this.insanity = -1.0;
	}

	public UpdateInsanity(double insanity)
	{
		this.insanity = insanity;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.insanity = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(insanity);
	}

	public static class Handler implements IMessageHandler<UpdateInsanity, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateInsanity message, MessageContext ctx)
		{
			System.out.println("Update Insanity Received!");
			Minecraft.getMinecraft().thePlayer.getEntityData().setDouble("PlayerInsanity", message.insanity);
			return null;
		}

	}
}