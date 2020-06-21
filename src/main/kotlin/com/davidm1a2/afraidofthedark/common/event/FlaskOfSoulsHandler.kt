package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.ItemFlaskOfSouls
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent

/**
 * Class used to detect mob death events and forward them to the flask of souls
 */
class FlaskOfSoulsHandler {
    /**
     * Called when a player crafts an item. If this item is a flask of souls unlock the research
     *
     * @param event The crafting event
     */
    @SubscribeEvent
    fun onItemCraftedEvent(event: ItemCraftedEvent) {
        // Server side processing only
        if (!event.player.world.isRemote) {
            // Test if the item crafted was a flask of souls
            if (event.crafting.item is ItemFlaskOfSouls) {
                // Grab the player's research
                val playerResearch = event.player.getResearch()

                // If the player can research phylactery of souls unlock it
                if (playerResearch.canResearch(ModResearches.PHYLACTERY_OF_SOULS)) {
                    playerResearch.setResearch(ModResearches.PHYLACTERY_OF_SOULS, true)
                    playerResearch.sync(event.player, true)
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
    fun onLivingDeathEvent(event: LivingDeathEvent) {
        // Ensure a player killed the entity. Make sure the entity is an entity living
        if (event.source.trueSource is EntityPlayer && event.entity is EntityLiving) {
            // Grab the killer player
            val entityPlayer = event.source.trueSource as EntityPlayer

            // Ensure the player has the right research
            if (entityPlayer.getResearch().isResearched(ModResearches.PHYLACTERY_OF_SOULS)) {
                // Grab the player's inventory
                val inventory = entityPlayer.inventory.mainInventory
                val entityID = EntityType.getId(event.entity.type)

                // Iterate over the player's inventory and look for flasks. If we find one test if we have a flask for the killed entity
                for (i in inventory.indices) {
                    // Grab the current itemstack
                    val itemStack = inventory[i]
                    // If the item is a flask process it
                    if (itemStack.item is ItemFlaskOfSouls) {
                        val flaskOfSouls = itemStack.item as ItemFlaskOfSouls
                        // If the flask is not complete and the killed entity was of the right type update this flask and return
                        if (!flaskOfSouls.isComplete(itemStack) && entityID == flaskOfSouls.getSpawnedEntity(itemStack)) {
                            // Add a kill and finish processing
                            flaskOfSouls.addKills(itemStack, 1)
                            return
                        }
                    }
                }

                // If we get here no flask of souls was able to take the entity so we need to test if any flask exists in the hotbar to take the entity
                // Iterate over the hotbar and find available flasks
                for (i in 0..8) {
                    // Grab the current itemstack
                    val itemStack = inventory[i]
                    // If the item is a flask process it
                    if (itemStack.item is ItemFlaskOfSouls) {
                        val flaskOfSouls = itemStack.item as ItemFlaskOfSouls
                        // If the flask is not complete and does not yet have a spawned entity set the spawned entity
                        if (!flaskOfSouls.isComplete(itemStack) && flaskOfSouls.getSpawnedEntity(itemStack) == null) {
                            // Set the spawned entity and add the kill to the flask
                            flaskOfSouls.setSpawnedEntity(itemStack, entityID)
                            flaskOfSouls.addKills(itemStack, 1)
                            return
                        }
                    }
                }
            }
        }
    }
}