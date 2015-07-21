/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
			Research.set(ctx.getServerHandler().playerEntity, message.research);
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
				Set<String> keysOriginal = Research.get(Minecraft.getMinecraft().thePlayer).getKeySet();
				for (Object key : message.research.getKeySet())
				{
					String keyString = (String) key;
					if (!Research.get(Minecraft.getMinecraft().thePlayer).getBoolean(keyString) && message.research.getBoolean(keyString))
					{
						Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:achievementUnlocked", 0.5f, 1.0f);
						ClientData.researchAchievedOverlay.displayResearch(ResearchTypes.valueOf(keyString.substring(Research.RESEARCH_DATA.length())), new ItemStack(ModItems.journal, 1), false);
					}
				}
			}
			Research.set(Minecraft.getMinecraft().thePlayer, message.research);
			return null;
		}
	}
}
