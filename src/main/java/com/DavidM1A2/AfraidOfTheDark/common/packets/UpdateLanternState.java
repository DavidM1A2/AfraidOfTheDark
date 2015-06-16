package com.DavidM1A2.AfraidOfTheDark.common.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

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
	public static class HandlerClient implements IMessageHandler<UpdateLanternState, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateLanternState message, final MessageContext ctx)
		{
			for (ItemStack itemStack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory)
			{
				if (itemStack != null && itemStack.getItem() instanceof ItemVitaeLantern)
				{
					if (NBTHelper.getBoolean(itemStack, "isActive"))
					{
						NBTHelper.setDouble(itemStack, "equalibriumPercentage", message.lanternState);
					}
				}
			}
			return null;
		}
	}

	// Upon receiving player insanity data update it on the player
	public static class HandlerServer implements IMessageHandler<UpdateLanternState, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateLanternState message, final MessageContext ctx)
		{
			for (ItemStack itemStack : ctx.getServerHandler().playerEntity.inventory.mainInventory)
			{
				if (itemStack != null && itemStack.getItem() instanceof ItemVitaeLantern)
				{
					if (NBTHelper.getBoolean(itemStack, "isActive"))
					{
						NBTHelper.setDouble(itemStack, "equalibriumPercentage", message.lanternState);
					}
				}
			}
			return new UpdateLanternState(message.lanternState);
		}
	}
}
