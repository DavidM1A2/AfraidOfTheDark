/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncVoidChest implements IMessage
{
	private int x = 0;
	private int y = 0;
	private int z = 0;
	private int entityIDToUpdate = 0;

	public SyncVoidChest()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.entityIDToUpdate = 0;
	}

	public SyncVoidChest(int x, int y, int z, int entityIDToUpdate)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.entityIDToUpdate = entityIDToUpdate;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.entityIDToUpdate = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(this.entityIDToUpdate);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerClient implements IMessageHandler<SyncVoidChest, IMessage>
	{
		@Override
		public IMessage onMessage(final SyncVoidChest message, final MessageContext ctx)
		{
			Entity toUpdate = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityIDToUpdate);
			if (toUpdate != null && toUpdate instanceof EntityPlayer)
			{
				if (toUpdate.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z)) != null)
				{
					TileEntity tileEntity = toUpdate.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (tileEntity instanceof TileEntityVoidChest)
					{
						((TileEntityVoidChest) tileEntity).openChest((EntityPlayer) toUpdate);
					}
				}
			}
			else
			{
				LogHelper.info("Null entity to update chest for");
			}
			return null;
		}
	}
}