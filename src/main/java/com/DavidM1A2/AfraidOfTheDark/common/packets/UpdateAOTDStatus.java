/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// This is an UpdateAOTD packet that is sent to a client or from a client to the server
// that updates the status of if the player has begun the mod
public class UpdateAOTDStatus implements IMessage
{
	private boolean started;

	public UpdateAOTDStatus()
	{
		this.started = false;
	}

	public UpdateAOTDStatus(final boolean started)
	{
		this.started = started;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.started = buf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeBoolean(this.started);
	}

	public static class Handler extends MessageHandler.Bidirectional<UpdateAOTDStatus>
	{
		@Override
		public IMessage handleClientMessage(EntityPlayer player, UpdateAOTDStatus msg, MessageContext ctx)
		{
			AOTDPlayerData.get(player).setHasStartedAOTD(msg.started);
			return null;
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, UpdateAOTDStatus msg, MessageContext ctx)
		{
			AOTDPlayerData.get(player).setHasStartedAOTD(msg.started);
			return null;
		}
	}
}