/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
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
	public static class Handler implements IMessageHandler<UpdateInsanity, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateInsanity message, final MessageContext ctx)
		{
			// For whatever stupid reason, minecraft's EntityPlayerSP object takes a few moments to get initialized so we wait for that
			while (Minecraft.getMinecraft().thePlayer == null)
			{
				try
				{
					Thread.sleep(50);
				}
				catch (final InterruptedException e)
				{
					System.out.println("Error Sleeping?");
				}
			}
			Minecraft.getMinecraft().thePlayer.getEntityData().setDouble("PlayerInsanity", message.insanity);
			return null;
		}

	}
}