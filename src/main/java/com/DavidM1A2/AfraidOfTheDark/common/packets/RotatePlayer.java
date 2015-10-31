/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RotatePlayer implements IMessage
{
	private int degrees;

	public RotatePlayer()
	{
		degrees = 0;
	}

	public RotatePlayer(int degrees)
	{
		this.degrees = degrees;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.degrees = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(this.degrees);
	}

	// When we get a packet we set the current item that the player is holding to the data we rececived
	public static class Handler extends MessageHandler.Client<RotatePlayer>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final RotatePlayer msg, final MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					entityPlayer.rotationYaw = entityPlayer.rotationYaw + msg.degrees;
				}
			});
			return null;
		}
	}
}
