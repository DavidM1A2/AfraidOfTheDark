/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.CreateMeteor;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TellServerToCreateMeteor implements IMessage
{
	private BlockPos thePosition;
	private int radius;
	private int height;
	private int index;

	public TellServerToCreateMeteor()
	{
		this.thePosition = null;
		this.radius = 0;
		this.height = 0;
		this.index = -1;
	}

	public TellServerToCreateMeteor(final BlockPos thePosition, final int radius, final int height, final int index)
	{
		this.thePosition = thePosition;
		this.radius = radius;
		this.height = height;
		this.index = index;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.thePosition = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.radius = buf.readInt();
		this.height = buf.readInt();
		this.index = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(this.thePosition.getX());
		buf.writeInt(this.thePosition.getY());
		buf.writeInt(this.thePosition.getZ());
		buf.writeInt(this.radius);
		buf.writeInt(this.height);
		buf.writeInt(this.index);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerServer implements IMessageHandler<TellServerToCreateMeteor, IMessage>
	{
		@Override
		public IMessage onMessage(final TellServerToCreateMeteor message, final MessageContext ctx)
		{
			LogHelper.info("Player has requested to place a meteor at " + message.thePosition.toString());
			CreateMeteor.create(ctx.getServerHandler().playerEntity.worldObj, message.thePosition, message.radius, message.height, false, true, MeteorTypes.typeFromIndex(message.index));
			return null;
		}
	}
}
