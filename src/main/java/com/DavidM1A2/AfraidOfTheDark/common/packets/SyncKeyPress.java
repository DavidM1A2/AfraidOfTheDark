/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncKeyPress implements IMessage
{
	private String characterName;

	public SyncKeyPress()
	{
		this.characterName = "";
	}

	public SyncKeyPress(final String characterName)
	{
		this.characterName = characterName;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.characterName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, characterName);
	}

	public static class Handler extends MessageHandler.Server<SyncKeyPress>
	{
		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final SyncKeyPress msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					player.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().keyPressed(msg.characterName);
				}
			});
			return null;
		}
	}
}