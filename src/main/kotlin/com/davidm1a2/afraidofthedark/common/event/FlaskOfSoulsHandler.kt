package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class used to detect mob death events and forward them to the flask of souls
 */
class FlaskOfSoulsHandler {
    /**
     * Called when an entity dies
     *
     * @param event The event containing death info
     */
    @SubscribeEvent
    fun onLivingDeathEvent(event: LivingDeathEvent) {
        // Ensure a player killed the entity. Make sure the entity is an entity living
        if (event.source.entity is Player && event.entity is LivingEntity) {
            // Grab the killer player
            val entityPlayer = event.source.entity as Player
            val flaskOfSouls = ModItems.FLASK_OF_SOULS

            // Ensure the player has the right research
            if (entityPlayer.getResearch().isResearched(ModResearches.FLASK_OF_SOULS)) {
                // Grab the player's inventory
                val inventory = entityPlayer.inventory.items + entityPlayer.inventory.offhand
                val entityID = EntityType.getKey(event.entity.type)

                if (!flaskOfSouls.supportsEntity(entityID)) {
                    return
                }

                // Iterate over the player's inventory and look for flasks. If we find one test if we have a flask for the killed entity
                for (itemStack in inventory) {
                    // If the item is a flask process it
                    if (itemStack.item == flaskOfSouls) {
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
                    if (itemStack.item == flaskOfSouls) {
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