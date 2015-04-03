/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

// Packet to update if a crossbow is cocked or not upon relog
public class UpdateCrossbow implements IMessage
{
	private NBTTagCompound crossbowData;

	public UpdateCrossbow()
	{
		this.crossbowData = null;
	}

	public UpdateCrossbow(NBTTagCompound crossbowData)
	{
		this.crossbowData = crossbowData;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.crossbowData = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, crossbowData);
	}

	// When we get a packet we set the current item that the player is holding to the data we rececived
	public static class Handler implements IMessageHandler<UpdateCrossbow, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateCrossbow message, MessageContext ctx)
		{
			LogHelper.info("Update Crossbow Received!");
			((EntityPlayer) ctx.getServerHandler().playerEntity).inventory.getCurrentItem().stackTagCompound = message.crossbowData;
			return null;
		}

	}
}
