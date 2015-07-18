/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
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
	public static class HandlerServer implements IMessageHandler<UpdateVitae, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateVitae message, final MessageContext ctx)
		{
			LogHelper.info("Update Vitae Status: " + message.vitaeLevel + " on entity " + ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityIDToUpdate).getName());
			ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityIDToUpdate).getEntityData().setInteger(Vitae.VITAE_LEVEL, message.vitaeLevel);
			return null;
		}
	}

	// when we receive a packet we set HasStartedAOTD
	public static class HandlerClient implements IMessageHandler<UpdateVitae, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateVitae message, final MessageContext ctx)
		{
			Entity toUpdate = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityIDToUpdate);
			if (toUpdate != null)
			{
				toUpdate.getEntityData().setInteger(Vitae.VITAE_LEVEL, message.vitaeLevel);
			}
			return null;
		}
	}
}
