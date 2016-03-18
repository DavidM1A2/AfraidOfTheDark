/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
		NBTTagCompound spellManagerData = ByteBufUtils.readTag(buf);
		this.spellManager = new SpellManager();
		this.spellManager.readFromNBT(spellManagerData);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		NBTTagCompound spellManagerData = new NBTTagCompound();
		this.spellManager.writeToNBT(spellManagerData);
		ByteBufUtils.writeTag(buf, spellManagerData);
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