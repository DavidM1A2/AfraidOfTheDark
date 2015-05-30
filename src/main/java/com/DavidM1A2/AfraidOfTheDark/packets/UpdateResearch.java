/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;

public class UpdateResearch implements IMessage
{
	private NBTTagCompound research;

	public UpdateResearch()
	{
		this.research = null;
	}

	public UpdateResearch(final NBTTagCompound research)
	{
		this.research = research;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.research = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.research);
	}

	// when we receive a packet we sets some research
	public static class HandlerServer implements IMessageHandler<UpdateResearch, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateResearch message, final MessageContext ctx)
		{
			LoadResearchData.set(ctx.getServerHandler().playerEntity, message.research);
			return null;
		}
	}

	// when we receive a packet we set some research
	public static class HandlerClient implements IMessageHandler<UpdateResearch, IMessage>
	{
		@Override
		public IMessage onMessage(final UpdateResearch message, final MessageContext ctx)
		{
			LoadResearchData.set(Minecraft.getMinecraft().thePlayer, message.research);
			return null;
		}
	}
}
