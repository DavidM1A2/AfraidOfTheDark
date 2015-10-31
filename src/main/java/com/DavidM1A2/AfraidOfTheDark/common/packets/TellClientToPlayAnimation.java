/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TellClientToPlayAnimation implements IMessage
{
	private String animationName = "";
	private int entityIDToUpdate = 0;

	public TellClientToPlayAnimation()
	{
		this.animationName = "";
		this.entityIDToUpdate = 0;
	}

	public TellClientToPlayAnimation(String animationName, int entityIDToUpdate)
	{
		this.animationName = animationName;
		this.entityIDToUpdate = entityIDToUpdate;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.animationName = ByteBufUtils.readUTF8String(buf);
		this.entityIDToUpdate = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.animationName);
		buf.writeInt(this.entityIDToUpdate);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class Handler extends MessageHandler.Client<TellClientToPlayAnimation>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final TellClientToPlayAnimation msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					Entity toUpdate = entityPlayer.worldObj.getEntityByID(msg.entityIDToUpdate);
					if (toUpdate != null)
					{
						if (toUpdate instanceof IMCAnimatedEntity)
						{
							((IMCAnimatedEntity) toUpdate).getAnimationHandler().activateAnimation(msg.animationName, 0);
						}
					}
					else
					{
						LogHelper.info("Null entity to play animation for");
					}
				}
			});
			return null;
		}
	}
}