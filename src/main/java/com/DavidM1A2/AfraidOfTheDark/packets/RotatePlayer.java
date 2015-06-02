package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RotatePlayer implements IMessage
{
	private int degrees;

	public RotatePlayer()
	{
		degrees = 0;
	}

	public RotatePlayer(int degrees)
	{
		this.degrees = degrees;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.degrees = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(this.degrees);
	}

	// When we get a packet we set the current item that the player is holding to the data we rececived
	public static class HandlerClient implements IMessageHandler<RotatePlayer, IMessage>
	{
		@Override
		public IMessage onMessage(final RotatePlayer message, final MessageContext ctx)
		{
			Minecraft.getMinecraft().thePlayer.rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw + message.degrees;
			return null;
		}
	}
}
