package com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Packet used to sync research between server and client
 */
public class SyncResearch implements IMessage
{
	private Map<Research, Boolean> researchToUnlocked;

	public SyncResearch()
	{
		this.researchToUnlocked = new HashMap<>();
	}

	public SyncResearch(Map<Research, Boolean> researchToUnlocked)
	{
		this.researchToUnlocked = researchToUnlocked;
	}

	/**
	 * Converts from the byte buffer into the structured research map
	 *
	 * @param buf The buffer to read
	 */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		// Read the compound from the buffer
		NBTTagCompound data = ByteBufUtils.readTag(buf);
		// For each research read our compound to test if it is researched or not
		for (Research research : ModRegistries.RESEARCH)
			this.researchToUnlocked.put(research, data.getBoolean(research.getRegistryName().toString()));
	}

	/**
	 * Writes the structured research map into a byte buffer
	 *
	 * @param buf The buffer to read
	 */
	@Override
	public void toBytes(ByteBuf buf)
	{
		// Create a compound to write to
		NBTTagCompound data = new NBTTagCompound();
		// For each research write a boolean if that research is researched
		this.researchToUnlocked.forEach((research, researched) -> data.setBoolean(research.getRegistryName().toString(), researched));
		// Write the compound
		ByteBufUtils.writeTag(buf, data);
	}

	/**
	 * Handler to perform actions upon getting a packet
	 */
	public static class Handler extends MessageHandler.Bidirectional<SyncResearch>
	{
		/**
		 * Handles the packet on client side
		 *
		 * @param player the player reference (the player who received the packet)
		 * @param msg the message received
		 * @param ctx the message context object. This contains additional information about the packet.
		 * @return null (no response)
		 */
		@Override
		public IMessage handleClientMessage(EntityPlayer player, SyncResearch msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				// Grab the player's current research, we must use Minecraft.getMinecraft().player because the player passed to use might be null
				IAOTDPlayerResearch playerResearch = Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
				AfraidOfTheDark.INSTANCE.getLogger().info("CLIENT: " + msg.researchToUnlocked.toString());
				// Iterate over the new research
				msg.researchToUnlocked.forEach((research, researched) ->
				{
					// If the research was not researched and it now is researched show the popup
					boolean wasResearched = playerResearch.isResearched(research);
					boolean showPopup = researched && !wasResearched;
					// Set the research
					if (showPopup)
						playerResearch.setResearchAndAlert(research, researched, Minecraft.getMinecraft().player);
					else
						playerResearch.setResearch(research, researched);
				});
			});
			return null;
		}

		/**
		 * Handles the packet on server side
		 *
		 * @param player the player reference (the player who sent the packet)
		 * @param msg the message received
		 * @param ctx the message context object. This contains additional information about the packet.
		 * @return null (no response)
		 */
		@Override
		public IMessage handleServerMessage(EntityPlayer player, SyncResearch msg, MessageContext ctx)
		{
			player.world.getMinecraftServer().addScheduledTask(() ->
			{
				AfraidOfTheDark.INSTANCE.getLogger().info("SERVER: " + msg.researchToUnlocked.toString());
				// Grab the player's current research
				IAOTDPlayerResearch playerResearch = player.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
				// Update each research, don't show popups server side
				msg.researchToUnlocked.forEach((playerResearch::setResearch));
			});
			return null;
		}
	}
}
