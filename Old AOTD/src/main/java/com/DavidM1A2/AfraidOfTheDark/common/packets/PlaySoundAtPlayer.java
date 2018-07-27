/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.client.globalSounds.BellsRinging;
import com.DavidM1A2.AfraidOfTheDark.client.globalSounds.ErieEcho;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDPlayerFollowSounds;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlaySoundAtPlayer implements IMessage
{
	private int delay;
	private AOTDPlayerFollowSounds followSounds;

	public PlaySoundAtPlayer()
	{
	}

	public PlaySoundAtPlayer(int delay, AOTDPlayerFollowSounds followSounds)
	{
		this.delay = delay;
		this.followSounds = followSounds;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.delay = buf.readInt();
		this.followSounds = this.followSounds.valueOf(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.delay);
		ByteBufUtils.writeUTF8String(buf, this.followSounds.toString());
	}

	public static class Handler extends MessageHandler.Client<PlaySoundAtPlayer>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final PlaySoundAtPlayer msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					switch (msg.followSounds)
					{
						case BellsRinging:
							Minecraft.getMinecraft().getSoundHandler().playDelayedSound(new BellsRinging(), msg.delay);
							break;
						case ErieEcho:
							Minecraft.getMinecraft().getSoundHandler().playDelayedSound(new ErieEcho(), msg.delay);
							break;
						default:
							LogHelper.error("Unrecognized sound " + msg.followSounds.toString());
							break;
					}
				}
			});
			return null;
		}
	}
}
