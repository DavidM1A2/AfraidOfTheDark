
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateHasBeatenEnaria implements IMessage
{
	private boolean hasBeatenEnaria;

	public UpdateHasBeatenEnaria()
	{
		this.hasBeatenEnaria = false;
	}

	public UpdateHasBeatenEnaria(final boolean hasBeatenEnaria)
	{
		this.hasBeatenEnaria = hasBeatenEnaria;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.hasBeatenEnaria = buf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeBoolean(this.hasBeatenEnaria);
	}

	public static class Handler extends MessageHandler.Client<UpdateHasBeatenEnaria>
	{
		@Override
		public IMessage handleClientMessage(EntityPlayer player, UpdateHasBeatenEnaria msg, MessageContext ctx)
		{
			AOTDPlayerData.get(player).setHasBeatenEnaria(msg.hasBeatenEnaria);
			return null;
		}
	}
}