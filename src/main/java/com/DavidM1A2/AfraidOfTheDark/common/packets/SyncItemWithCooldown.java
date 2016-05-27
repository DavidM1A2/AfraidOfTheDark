/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemWithCooldownPerItem;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncItemWithCooldown implements IMessage
{
	private long timeServer;
	private AOTDItemWithCooldownPerItem itemToSync;

	public SyncItemWithCooldown()
	{
		// -1.0 = something went wrong
		this.timeServer = -1;
		this.itemToSync = null;
	}

	public SyncItemWithCooldown(final long timeServer, final AOTDItemWithCooldownPerItem itemToSync)
	{
		this.timeServer = timeServer;
		this.itemToSync = itemToSync;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.timeServer = buf.readLong();
		this.itemToSync = (AOTDItemWithCooldownPerItem) Item.getItemById(buf.readInt());
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeLong(this.timeServer);
		buf.writeInt(Item.getIdFromItem(this.itemToSync));
	}

	// Upon receiving player insanity data update it on the player
	public static class Handler extends MessageHandler.Client<SyncItemWithCooldown>
	{
		@Override
		public IMessage handleClientMessage(EntityPlayer entityPlayer, final SyncItemWithCooldown msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					msg.itemToSync.setServerClientDifference(System.currentTimeMillis() - msg.timeServer);
				}
			});
			return null;
		}
	}
}