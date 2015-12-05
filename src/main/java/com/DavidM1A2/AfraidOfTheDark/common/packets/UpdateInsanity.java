/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// Update player insanity upon relog
public class UpdateInsanity implements IMessage
{
	private double insanity;

	public UpdateInsanity()
	{
		// -1.0 = something went wrong
		this.insanity = -1.0;
	}

	public UpdateInsanity(final double insanity)
	{
		this.insanity = insanity;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.insanity = buf.readDouble();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeDouble(this.insanity);
	}

	// Upon receiving player insanity data update it on the player
	public static class Handler extends MessageHandler.Client<UpdateInsanity>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final UpdateInsanity msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					if (ConfigurationHandler.debugMessages)
					{
						LogHelper.info("Updating insanity: " + msg.insanity);
					}
					AOTDPlayerData.get(entityPlayer).setPlayerInsanity(msg.insanity);
				}
			});
			return null;
		}

	}
}