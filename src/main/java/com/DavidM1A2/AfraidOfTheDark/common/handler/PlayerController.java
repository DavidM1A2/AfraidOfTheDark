/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModPotionEffects;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellPlayerHesSleeping;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.InventorySaver;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedAOTDUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedInsanityUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedResearchUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedTeleport;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedVitaeUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class PlayerController
{

	// When the player dies, he is cloned. We move over extended properties such as hasStartedAOTD, insanity, and research
	@SubscribeEvent
	public void onClonePlayer(final PlayerEvent.Clone event)
	{
		final boolean hasStartedAOTD = HasStartedAOTD.get(event.original);
		HasStartedAOTD.set(event.entityPlayer, hasStartedAOTD, Side.SERVER);
		final double insanity = Insanity.get(event.original);
		Insanity.addInsanity(insanity, event.entityPlayer);
		final NBTTagCompound research = LoadResearchData.get(event.original);
		LoadResearchData.set(event.entityPlayer, research);
		final int vitaeLevel = Vitae.get(event.original);
		Vitae.set(event.entityPlayer, vitaeLevel, Side.SERVER);
		if (event.original.dimension == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			InventorySaver.setInventory(event.entityPlayer, InventorySaver.getInventory(event.original), InventorySaver.getPlayerLocation(event.original));
			(new DelayedTeleport(event.entityPlayer, 0)).start();
		}
		// When the player gets new research we will wait 500ms before updating because otherwise the event.original player
		// will get the new data
		(new DelayedAOTDUpdate(event.entityPlayer, hasStartedAOTD)).start();
		(new DelayedInsanityUpdate(event.entityPlayer, insanity)).start();
		(new DelayedResearchUpdate(event.entityPlayer, research)).start();
		(new DelayedVitaeUpdate(event.entityPlayer, vitaeLevel)).start();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEvent(final FogDensity event)
	{
		if (event.entity instanceof EntityPlayer && event.entity.dimension == 0)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			final float insanity = (float) Insanity.get(entityPlayer);

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
	public void onEntityJoinWorld(final EntityJoinWorldEvent event)
	{
		// When the player joins the world
		if (event.entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			/*
			 * This first block of code will update any crossbows when the player loads in.
			 */
			for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
			{
				final ItemStack currentStack = entityPlayer.inventory.getStackInSlot(i);
				if (currentStack != null)
				{
					if (currentStack.getItem() instanceof ItemCrossbow)
					{
						final ItemCrossbow currentItem = (ItemCrossbow) currentStack.getItem();

						currentStack.setTagCompound(currentItem.loadNBTData(currentStack));
					}
				}
			}

			/*
			 * Sync player research, insanity, and AOTDStart status
			 */
			if (!event.world.isRemote)
			{
				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(Insanity.get(entityPlayer)), (EntityPlayerMP) entityPlayer);

				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(HasStartedAOTD.get(entityPlayer)), (EntityPlayerMP) entityPlayer);

				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(LoadResearchData.get(entityPlayer), false), (EntityPlayerMP) entityPlayer);
			}
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(final EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			/*
			 * This first block of code will load the player's research.
			 */
			if (entityPlayer.getExtendedProperties(LoadResearchData.RESEARCH_DATA) == null)
			{
				LoadResearchData.register(entityPlayer);
			}
			/*
			 * This second block of code will check if the player has begun the mod.
			 */
			if (entityPlayer.getExtendedProperties(HasStartedAOTD.PLAYER_STARTED_AOTD) == null)
			{
				HasStartedAOTD.register(entityPlayer);
			}
			/*
			 * This third block of code will determine if the player has insanity yet, and if he/she does we will load it.
			 */
			if (entityPlayer.getExtendedProperties(Insanity.PLAYER_INSANITY) == null)
			{
				Insanity.register(entityPlayer);
			}

			if (entityPlayer.getExtendedProperties(InventorySaver.INVENTORY_SAVER) == null)
			{
				InventorySaver.register(entityPlayer);
			}
		}

		if (event.entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLivingBase = (EntityLivingBase) event.entity;
			if (entityLivingBase.getExtendedProperties(Vitae.VITAE_LEVEL) == null)
			{
				Vitae.register(entityLivingBase);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event)
	{
		if (!event.entityPlayer.worldObj.isRemote)
		{
			if (event.entityPlayer.getActivePotionEffect(ModPotionEffects.sleepingPotion) != null)
			{
				if (LoadResearchData.canResearch(event.entityPlayer, ResearchTypes.Nightmares))
				{
					LoadResearchData.unlockResearchSynced(event.entityPlayer, ResearchTypes.Nightmares, Side.SERVER, true);
				}
				Utility.sendPlayerToDimension((EntityPlayerMP) event.entityPlayer, Constants.NightmareWorld.NIGHTMARE_WORLD_ID, false);
				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new TellPlayerHesSleeping(), (EntityPlayerMP) event.entityPlayer);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlayEventChat(final RenderGameOverlayEvent.Chat event)
	{
		if (ClientData.researchAchievedOverlay != null)
		{
			ClientData.researchAchievedOverlay.updateResearchAchievedWindow();
		}
	}

	@SubscribeEvent
	public void onEntityInteractEvent(final EntityInteractEvent event)
	{
		if (event.target instanceof EntityDeeeSyft)
		{
			if (event.entityPlayer.inventory.getCurrentItem().getItem() instanceof ItemFlintAndSteel)
			{
				event.target.setFire(1);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerChangedDimensionEvent(final PlayerChangedDimensionEvent event)
	{
		EntityPlayer entityPlayer = event.player;
		if (event.toDim == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			InventorySaver.saveInventory(entityPlayer);
			entityPlayer.inventory.clear();
		}
		else if (event.fromDim == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			InventorySaver.loadInventory(entityPlayer);
			InventorySaver.resetSavedInventory(entityPlayer);
		}
	}
}
