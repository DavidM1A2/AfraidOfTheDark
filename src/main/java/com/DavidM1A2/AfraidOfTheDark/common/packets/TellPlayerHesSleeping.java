package com.DavidM1A2.AfraidOfTheDark.common.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

public class TellPlayerHesSleeping implements IMessage
{
	public TellPlayerHesSleeping()
	{
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerClient implements IMessageHandler<TellPlayerHesSleeping, IMessage>
	{
		@Override
		public IMessage onMessage(final TellPlayerHesSleeping message, final MessageContext ctx)
		{
			LogHelper.info("I'm sleeping and changing dimensions!");
			Minecraft.getMinecraft().thePlayer.travelToDimension(Constants.NightmareWorld.NIGHTMARE_WORLD_ID);
			return null;
		}
	}
}
