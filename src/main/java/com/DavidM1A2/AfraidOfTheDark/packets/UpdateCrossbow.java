package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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

	public static class Handler implements IMessageHandler<UpdateCrossbow, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateCrossbow message, MessageContext ctx)
		{
			System.out.println("Update Crossbow Received!");
			((EntityPlayer) ctx.getServerHandler().playerEntity).inventory.getCurrentItem().stackTagCompound = message.crossbowData;
			return null;
		}

	}
}
