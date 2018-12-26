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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncHasEnariasAltar implements IMessage
{
	private boolean hasAltar;

	public SyncHasEnariasAltar()
	{
		this.hasAltar = false;
	}

	public SyncHasEnariasAltar(final boolean hasAltar)
	{
		this.hasAltar = hasAltar;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.hasAltar = buf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeBoolean(this.hasAltar);
	}

	public static class Handler extends MessageHandler.Client<SyncHasEnariasAltar>
	{
		@Override
		public IMessage handleClientMessage(EntityPlayer player, SyncHasEnariasAltar msg, MessageContext ctx)
		{
			player.getCapability(ModCapabilities.PLAYER_DATA, null).setHasEnariasAltar(msg.hasAltar);
			return null;
		}
	}
}