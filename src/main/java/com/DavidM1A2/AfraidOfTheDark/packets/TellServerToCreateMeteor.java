package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.worldGeneration.CreateMeteor;

public class TellServerToCreateMeteor implements IMessage
{
	private BlockPos thePosition;
	private int radius;
	private int height;
	private int index;

	public TellServerToCreateMeteor()
	{
		thePosition = null;
		radius = 0;
		height = 0;
		index = -1;
	}

	public TellServerToCreateMeteor(BlockPos thePosition, int radius, int height, int index)
	{
		this.thePosition = thePosition;
		this.radius = radius;
		this.height = height;
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.thePosition = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.radius = buf.readInt();
		this.height = buf.readInt();
		this.index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
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
		public IMessage onMessage(TellServerToCreateMeteor message, MessageContext ctx)
		{
			LogHelper.info("Player has requested to place a meteor at " + message.thePosition.toString());
			CreateMeteor.create(ctx.getServerHandler().playerEntity.worldObj, message.thePosition, message.radius, message.height, false, true, MeteorTypes.typeFromIndex(message.index));
			return null;
		}
	}
}
