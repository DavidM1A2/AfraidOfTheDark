/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData.IAOTDEntityData;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncAOTDEntityData implements IMessage
{
	private NBTTagCompound data;

	public SyncAOTDEntityData()
	{
	}

	public SyncAOTDEntityData(IAOTDEntityData entityData)
	{
		this.data = (NBTTagCompound) ModCapabilities.ENTITY_DATA.getStorage().writeNBT(ModCapabilities.ENTITY_DATA, entityData, null);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.data);
	}

	public static class Handler extends MessageHandler.Bidirectional<SyncAOTDEntityData>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer player, final SyncAOTDEntityData msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					ModCapabilities.ENTITY_DATA.getStorage().readNBT(ModCapabilities.ENTITY_DATA, player.getCapability(ModCapabilities.ENTITY_DATA, null), null, msg.data);
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer player, SyncAOTDEntityData msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					player.getCapability(ModCapabilities.ENTITY_DATA, null).syncAll();
				}
			});
			return null;
		}
	}
}
