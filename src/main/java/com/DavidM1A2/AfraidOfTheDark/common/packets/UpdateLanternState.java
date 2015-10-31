/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateLanternState implements IMessage
{
	private double lanternState;

	public UpdateLanternState()
	{
		this.lanternState = -1.0;
	}

	public UpdateLanternState(final double lanternState)
	{
		this.lanternState = lanternState;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.lanternState = buf.readDouble();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeDouble(this.lanternState);
	}

	// Upon receiving player insanity data update it on the player
	public static class Handler extends MessageHandler.Bidirectional<UpdateLanternState>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final UpdateLanternState msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
					{
						if (itemStack != null && itemStack.getItem() instanceof ItemVitaeLantern)
						{
							if (NBTHelper.getBoolean(itemStack, "isActive"))
							{
								NBTHelper.setDouble(itemStack, "equalibriumPercentage", msg.lanternState);
							}
						}
					}
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, final UpdateLanternState msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
					{
						if (itemStack != null && itemStack.getItem() instanceof ItemVitaeLantern)
						{
							if (NBTHelper.getBoolean(itemStack, "isActive"))
							{
								NBTHelper.setDouble(itemStack, "equalibriumPercentage", msg.lanternState);
							}
						}
					}
				}
			});
			return null;
		}
	}
}
