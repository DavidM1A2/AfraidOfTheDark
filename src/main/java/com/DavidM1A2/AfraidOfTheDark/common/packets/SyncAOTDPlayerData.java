/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncAOTDPlayerData implements IMessage
{
	private NBTTagCompound data;

	public SyncAOTDPlayerData()
	{
	}

	public SyncAOTDPlayerData(AOTDPlayerData playerData)
	{
		this.data = new NBTTagCompound();
		playerData.saveNBTData(this.data);
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

	public static class Handler extends MessageHandler.Bidirectional<SyncAOTDPlayerData>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer player, final SyncAOTDPlayerData msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					AOTDPlayerData.get(player).loadNBTData(msg.data);
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer player, SyncAOTDPlayerData msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					AOTDPlayerData.get(player).syncAll();
				}
			});
			return null;
		}
	}
}
