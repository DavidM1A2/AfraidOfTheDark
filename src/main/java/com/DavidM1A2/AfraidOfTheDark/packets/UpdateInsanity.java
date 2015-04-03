/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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
			LogHelper.info("Update Insanity Received!");
			Minecraft.getMinecraft().thePlayer.getEntityData().setDouble("PlayerInsanity", message.insanity);
			return null;
		}

	}
}