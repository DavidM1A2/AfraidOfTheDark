/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
	public static class Handler extends MessageHandler.Bidirectional<UpdateResearch>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final UpdateResearch msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					Set<String> keysOriginal = AOTDPlayerData.get(entityPlayer).getResearches().getKeySet();
					for (Object key : msg.research.getKeySet())
					{
						String keyString = (String) key;
						if (!AOTDPlayerData.get(entityPlayer).getResearches().getBoolean(keyString) && msg.research.getBoolean(keyString))
						{
							entityPlayer.playSound("afraidofthedark:achievementUnlocked", 1.0f, 1.0f);
							ClientData.researchAchievedOverlay.displayResearch(ResearchTypes.valueOf(keyString.substring("unlockedResearches".length())), new ItemStack(ModItems.journal, 1), false);
						}
					}

					AOTDPlayerData.get(entityPlayer).setReseraches(msg.research);
					if (ConfigurationHandler.debugMessages)
					{
						LogHelper.info("Update research packet received, " + msg.research.toString());
					}
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, final UpdateResearch msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					AOTDPlayerData.get(entityPlayer).setReseraches(msg.research);
				}
			});
			return null;
		}
	}
}
