/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateVitae implements IMessage
{
	private int vitaeLevel = 0;
	private int entityIDToUpdate = 0;

	public UpdateVitae()
	{
		this.vitaeLevel = 0;
		this.entityIDToUpdate = 0;
	}

	public UpdateVitae(int vitaeLevel, int entityIDToUpdate)
	{
		this.vitaeLevel = vitaeLevel;
		this.entityIDToUpdate = entityIDToUpdate;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.vitaeLevel = buf.readInt();
		this.entityIDToUpdate = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.vitaeLevel);
		buf.writeInt(this.entityIDToUpdate);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class Handler extends MessageHandler.Bidirectional<UpdateVitae>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final UpdateVitae msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					Entity toUpdate = entityPlayer.worldObj.getEntityByID(msg.entityIDToUpdate);
					if (toUpdate != null)
					{
						AOTDEntityData.get(toUpdate).setVitaeLevel(msg.vitaeLevel);
					}
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, final UpdateVitae msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					if (Constants.isDebug)
					{
						LogHelper.info("Update Vitae Status: " + msg.vitaeLevel + " on entity " + entityPlayer.worldObj.getEntityByID(msg.entityIDToUpdate).getName());
					}
					AOTDEntityData.get(entityPlayer.worldObj.getEntityByID(msg.entityIDToUpdate)).setVitaeLevel(msg.vitaeLevel);
				}
			});
			return null;
		}
	}
}
