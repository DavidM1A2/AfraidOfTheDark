/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

// Update player insanity upon relog
public class UpdateInsanity implements IMessage
{
	private double insanity;

	public UpdateInsanity()
	{
		// -1.0 = something went wrong
		this.insanity = -1.0;
	}

	public UpdateInsanity(double insanity)
	{
		this.insanity = insanity;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.insanity = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(insanity);
	}

	// Upon receiving player insanity data update it on the player
	public static class Handler implements IMessageHandler<UpdateInsanity, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateInsanity message, MessageContext ctx)
		{
			// For whatever stupid reason, minecraft's EntityPlayerSP object takes a few moments to get initialized so we wait for that
			while (Minecraft.getMinecraft().thePlayer == null)
			{
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					System.out.println("Error Sleeping?");
				}
			}
			LogHelper.info("Update Insanity Received! Status: " + message.insanity);
			Minecraft.getMinecraft().thePlayer.getEntityData().setDouble("PlayerInsanity", message.insanity);
			return null;
		}

	}
}