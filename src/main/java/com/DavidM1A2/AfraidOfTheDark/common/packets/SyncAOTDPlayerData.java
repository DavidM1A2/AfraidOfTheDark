/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.IAOTDPlayerData;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncAOTDPlayerData implements IMessage
{
	private NBTTagCompound data;

	public SyncAOTDPlayerData()
	{
	}

	public SyncAOTDPlayerData(IAOTDPlayerData playerData)
	{
		this.data = (NBTTagCompound) ModCapabilities.PLAYER_DATA.getStorage().writeNBT(ModCapabilities.PLAYER_DATA, playerData, null);
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
					ModCapabilities.PLAYER_DATA.getStorage().readNBT(ModCapabilities.PLAYER_DATA, player.getCapability(ModCapabilities.PLAYER_DATA, null), null, msg.data);
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, SyncAOTDPlayerData msg, MessageContext ctx)
		{
			entityPlayer.world.getMinecraftServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncAll();
				}
			});
			return null;
		}
	}
}
