/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FlaskOfSoulsUpdate implements IMessage
{
	private ItemStack flaskOfSouls;
	private long timeServer;

	public FlaskOfSoulsUpdate()
	{
		// -1.0 = something went wrong
		this.flaskOfSouls = null;
		this.timeServer = -1;
	}

	public FlaskOfSoulsUpdate(final ItemStack itemStack, final long timeServer)
	{
		this.flaskOfSouls = itemStack;
		this.timeServer = timeServer;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.flaskOfSouls = ByteBufUtils.readItemStack(buf);
		this.timeServer = buf.readLong();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeItemStack(buf, flaskOfSouls);
		buf.writeLong(timeServer);
	}

	// Upon receiving player insanity data update it on the player
	public static class HandlerClient implements IMessageHandler<FlaskOfSoulsUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(final FlaskOfSoulsUpdate message, final MessageContext ctx)
		{
			long timeDifference = System.currentTimeMillis() - message.timeServer;
			LogHelper.info(timeDifference);
			ModItems.flaskOfSouls.setServerClientDifference(timeDifference);
			return null;
		}
	}
}