package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.ItemFlaskOfSouls;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Class used to detect mob death events and forward them to the flask of souls
 */
public class FlaskOfSoulsHandler
{
	/**
	 * Called when a player crafts an item. If this item is a flask of souls unlock the research
	 *
	 * @param event The crafting event
	 */
	@SubscribeEvent
	public void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event)
	{
		// Server side processing only
		if (!event.player.world.isRemote)
		{
			// Test if the item crafted was a flask of souls
			if (event.crafting.getItem() instanceof ItemFlaskOfSouls)
			{
				// Grab the player's research
				IAOTDPlayerResearch playerResearch = event.player.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
				// If the player can research phylactery of souls unlock it
				if (playerResearch.canResearch(ModResearches.PHYLACTERY_OF_SOULS))
				{
					playerResearch.setResearch(ModResearches.PHYLACTERY_OF_SOULS, true);
					playerResearch.sync(event.player, true);
				}
			}
		}
	}

	/**
	 * Called when an entity dies
	 *
	 * @param event The event containing death info
	 */
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		// Ensure a player killed the entity
		if (event.getSource().getTrueSource() instanceof EntityPlayer)
		{
			// Grab the killer player
			EntityPlayer entityPlayer = (EntityPlayer) event.getSource().getTrueSource();
			// Ensure the player has the right research
			if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.PHYLACTERY_OF_SOULS))
			{
				// Grab the player's inventory
				NonNullList<ItemStack> inventory = entityPlayer.inventory.mainInventory;
				ResourceLocation entityID = EntityList.getKey(event.getEntity());
				// Iterate over the player's inventory and look for flasks. If we find one test if we have a flask for the killed entity
				for (int i = 0; i < inventory.size(); i++)
				{
					// Grab the current itemstack
					ItemStack itemStack = inventory.get(i);
					// If the item is a flask process it
					if (itemStack.getItem() instanceof ItemFlaskOfSouls)
					{
						ItemFlaskOfSouls flaskOfSouls = (ItemFlaskOfSouls) itemStack.getItem();
						// If the flask is not complete and the killed entity was of the right type update this flask and return
						if (!flaskOfSouls.isComplete(itemStack) && entityID.equals(flaskOfSouls.getSpawnedEntity(itemStack)))
						{
							// Add a kill and finish processing
							flaskOfSouls.addKills(itemStack, 1);
							return;
						}
					}
				}

				// If we get here no flask of souls was able to take the entity so we need to test if any flask exists in the hotbar to take the entity

				// Iterate over the hotbar and find available flasks
				for (int i = 0; i < 9; i++)
				{
					// Grab the current itemstack
					ItemStack itemStack = inventory.get(i);
					// If the item is a flask process it
					if (itemStack.getItem() instanceof ItemFlaskOfSouls)
					{
						ItemFlaskOfSouls flaskOfSouls = (ItemFlaskOfSouls) itemStack.getItem();
						// If the flask is not complete and does not yet have a spawned entity set the spawned entity
						if (!flaskOfSouls.isComplete(itemStack) && flaskOfSouls.getSpawnedEntity(itemStack) == null)
						{
							// Set the spawned entity and add the kill to the flask
							flaskOfSouls.setSpawnedEntity(itemStack, EntityList.getKey(event.getEntity()));
							flaskOfSouls.addKills(itemStack, 1);
							return;
						}
					}
				}
			}
		}
	}
}
