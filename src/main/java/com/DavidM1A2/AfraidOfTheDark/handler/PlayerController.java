/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.item.crossbow.ItemCrossbow;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.research.Research;

public class PlayerController
{

	// When the player dies, he is cloned. We move over extended properties such as hasStartedAOTD, insanity, and research
	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event)
	{
		boolean hasStartedAOTD = HasStartedAOTD.get(event.original);
		HasStartedAOTD.set(event.entityPlayer, hasStartedAOTD);
		double insanity = Insanity.get(event.original);
		Insanity.increaseInsanity(insanity, event.entityPlayer);
		Research research = LoadResearchData.get(event.original);
		LoadResearchData.set(event.entityPlayer, research);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEvent(FogDensity event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			float insanity = (float) Insanity.get(entityPlayer);

			// If the player is insane, set the fog equal to 1.001^(.5*insanity) - .9989
			if (insanity >= 0.1)
			{
				event.density = ((float) Math.pow(1.001, 0.5f * insanity) - .9989f);
				event.setCanceled(true);
			}
			else
			{
				event.density = 0f;
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		// When the player joins the world
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			/*
			 * This first block of code will determine if the player has insanity yet, and if he/she does we will load it.
			 */
			if (entityPlayer.getExtendedProperties("PlayerInsanity") == null)
			{
				entityPlayer.registerExtendedProperties("PlayerInsanity", new Insanity());
			}
			if (!event.world.isRemote)
			{
				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(Insanity.get(entityPlayer)), (EntityPlayerMP) entityPlayer);
			}

			/*
			 * This second block of code will update any crossbows when the player loads in.
			 */
			for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
			{
				ItemStack currentStack = entityPlayer.inventory.getStackInSlot(i);
				if (currentStack != null)
				{
					if (currentStack.getItem() instanceof ItemCrossbow)
					{
						ItemCrossbow currentItem = (ItemCrossbow) currentStack.getItem();

						currentStack.setTagCompound(currentItem.loadNBTData(currentStack));
					}
				}
			}

			/*
			 * This third block of code will check if the player has begun the mod.
			 */
			if (entityPlayer.getExtendedProperties("playerStartedAOTD") == null)
			{
				entityPlayer.registerExtendedProperties("playerStartedAOTD", new HasStartedAOTD());
			}
			if (!event.world.isRemote)
			{
				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(HasStartedAOTD.get(entityPlayer)), (EntityPlayerMP) entityPlayer);
			}

			if (!event.world.isRemote)
			{
				for (int i = 0; i < Research.getResearchAmount(); i++)
				{
					AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(i, LoadResearchData.get(entityPlayer).getResearches().get(i).isResearched()), (EntityPlayerMP) entityPlayer);
				}
			}

			if (event.world.isRemote)
			{

			}
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			/*
			 * This fourth block of code will load the player's research.
			 */
			if (entityPlayer.getExtendedProperties("unlockedResearches") == null)
			{
				LoadResearchData.register(entityPlayer);
			}
		}
	}
}
