/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// Packet to update if a crossbow is cocked or not upon relog
public class UpdateCrossbow implements IMessage
{
	private NBTTagCompound crossbowData;

	public UpdateCrossbow()
	{
		this.crossbowData = null;
	}

	public UpdateCrossbow(final NBTTagCompound crossbowData)
	{
		this.crossbowData = crossbowData;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.crossbowData = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.crossbowData);
	}

	// When we get a packet we set the current item that the player is holding to the data we rececived
	public static class Handler implements IMessageHandler<UpdateCrossbow, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateCrossbow message, final MessageContext ctx)
		{
			if (Constants.isDebug)
			{
				LogHelper.info("Update Crossbow Received!");
			}
			((EntityPlayer) ctx.getServerHandler().playerEntity).inventory.getCurrentItem().setTagCompound(message.crossbowData);
			return null;
		}

	}
}
