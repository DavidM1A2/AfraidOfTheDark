/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncFlaskOfSouls implements IMessage
{
	private ItemStack flaskOfSouls;
	private long timeServer;

	public SyncFlaskOfSouls()
	{
		// -1.0 = something went wrong
		this.timeServer = -1;
	}

	public SyncFlaskOfSouls(final long timeServer)
	{
		this.timeServer = timeServer;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.timeServer = buf.readLong();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeLong(timeServer);
	}

	// Upon receiving player insanity data update it on the player
	public static class Handler extends MessageHandler.Client<SyncFlaskOfSouls>
	{
		@Override
		public IMessage handleClientMessage(EntityPlayer entityPlayer, final SyncFlaskOfSouls msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					ModItems.flaskOfSouls.setServerClientDifference(System.currentTimeMillis() - msg.timeServer);
				}
			});
			return null;
		}
	}
}