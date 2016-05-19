/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncVoidChest extends AbstractEntitySync
{
	private int x = 0;
	private int y = 0;
	private int z = 0;

	public SyncVoidChest()
	{
		super();
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public SyncVoidChest(int x, int y, int z, Entity entityToUpdate)
	{
		super(entityToUpdate);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class Handler extends MessageHandler.Client<SyncVoidChest>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final SyncVoidChest msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					Entity toUpdate = entityPlayer.worldObj.getEntityByID(msg.entityIDToUpdate);
					if (toUpdate != null && toUpdate instanceof EntityPlayer)
					{
						if (toUpdate.worldObj.getTileEntity(new BlockPos(msg.x, msg.y, msg.z)) != null)
						{
							TileEntity tileEntity = toUpdate.worldObj.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
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
				}
			});
			return null;
		}
	}
}