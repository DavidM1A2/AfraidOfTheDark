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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncKeyPress implements IMessage
{
	private char character;
	private int characterID;

	public SyncKeyPress()
	{
		this.character = ' ';
		this.characterID = -1;
	}

	public SyncKeyPress(final char character, final int characterID)
	{
		this.character = character;
		this.characterID = characterID;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.character = buf.readChar();
		this.characterID = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeChar(this.character);
		buf.writeInt(this.characterID);
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
					player.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().keyPressed(msg.characterID, msg.character);
				}
			});
			return null;
		}
	}
}