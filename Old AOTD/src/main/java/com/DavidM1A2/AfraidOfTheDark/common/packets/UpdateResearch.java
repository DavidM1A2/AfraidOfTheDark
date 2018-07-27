/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
					Set<String> keysOriginal = entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getResearches().getKeySet();
					for (String key : msg.research.getKeySet())
					{
						if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getResearches().getBoolean(key) && msg.research.getBoolean(key))
						{
							entityPlayer.playSound(ModSounds.achievementUnlocked, 1.0f, 1.0f);
							ClientData.researchAchievedOverlay.displayResearch(ResearchTypes.valueOf(key.substring("unlockedResearches".length())), new ItemStack(ModItems.journal, 1), false);
						}
					}

					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setReseraches(msg.research);
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
			entityPlayer.world.getMinecraftServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setReseraches(msg.research);
				}
			});
			return null;
		}
	}
}
