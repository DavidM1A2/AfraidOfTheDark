/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTObjectWriter;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncSpellManager implements IMessage
{
	private SpellManager spellManager;

	public SyncSpellManager()
	{
		this.spellManager = null;
	}

	public SyncSpellManager(final SpellManager spellManager)
	{
		this.spellManager = spellManager;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		int numberOfBytes = buf.readInt();
		byte[] spellManagerBytes = new byte[numberOfBytes];
		buf.readBytes(spellManagerBytes);
		this.spellManager = (SpellManager) NBTObjectWriter.byteArrayToObject(spellManagerBytes);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		byte[] spellManagerBytes = NBTObjectWriter.objectToByteArray(this.spellManager);
		buf.writeInt(spellManagerBytes.length);
		buf.writeBytes(spellManagerBytes);
	}

	public static class Handler extends MessageHandler.Bidirectional<SyncSpellManager>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer player, final SyncSpellManager msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					AOTDPlayerData.get(player).setSpellManager(msg.spellManager);
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final SyncSpellManager msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					AOTDPlayerData.get(player).setSpellManager(msg.spellManager);
				}
			});
			return null;
		}
	}
}