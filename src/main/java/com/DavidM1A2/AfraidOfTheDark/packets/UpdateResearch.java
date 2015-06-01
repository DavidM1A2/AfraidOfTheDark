/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.packets;

import io.netty.buffer.ByteBuf;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class UpdateResearch implements IMessage
{
	private NBTTagCompound research;
	private boolean firstTimeResearched;

	public UpdateResearch()
	{
		this.research = null;
		this.firstTimeResearched = false;
	}

	public UpdateResearch(final NBTTagCompound research, boolean firstTimeResearched)
	{
		this.research = research;
		this.firstTimeResearched = firstTimeResearched;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.research = ByteBufUtils.readTag(buf);
		this.firstTimeResearched = buf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.research);
		buf.writeBoolean(this.firstTimeResearched);
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
			if (message.firstTimeResearched)
			{
				Set<String> keysOriginal = LoadResearchData.get(Minecraft.getMinecraft().thePlayer).getKeySet();
				for (Object key : message.research.getKeySet())
				{
					String keyString = (String) key;
					if (!LoadResearchData.get(Minecraft.getMinecraft().thePlayer).getBoolean(keyString) && message.research.getBoolean(keyString))
					{
						LogHelper.info(keyString.substring(LoadResearchData.RESEARCH_DATA.length()));
						Refrence.researchAchievedOverlay.displayResearch(ResearchTypes.valueOf(keyString.substring(LoadResearchData.RESEARCH_DATA.length())), new ItemStack(ModItems.journal, 1), false);
					}
				}
			}
			LoadResearchData.set(Minecraft.getMinecraft().thePlayer, message.research);
			return null;
		}
	}
}
