package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.math.ceil

/**
 * Class used to detect mob death events and forward them to the vitae lanterns
 */
class VitaeLanternHandler {
    /**
     * Called when an entity dies
     *
     * @param event The event containing death info
     */
    @SubscribeEvent
    fun onLivingDeathEvent(event: LivingDeathEvent) {
        // Ensure a player killed the entity. Make sure the entity is an entity living
        val killedEntity = event.entity
        if (event.source.entity is PlayerEntity && killedEntity is LivingEntity) {
            // Grab the killer player
            val entityPlayer = event.source.entity as PlayerEntity
            val vitaeLantern = ModItems.VITAE_LANTERN

            // Ensure the player has the right research
            if (entityPlayer.getResearch().isResearched(ModResearches.VITAE_LANTERN)) {
                // Grab the player's inventory
                val inventory = entityPlayer.inventory.items + entityPlayer.inventory.offhand
                var remainingVitaeToAdd = ceil(killedEntity.maxHealth * HP_TO_VITAE_RATIO)
                for (itemStack in inventory) {
                    if (itemStack.item == vitaeLantern) {
                        remainingVitaeToAdd = vitaeLantern.addVitae(itemStack, remainingVitaeToAdd)
                        if (remainingVitaeToAdd == 0f) {
                            return
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val HP_TO_VITAE_RATIO = 1.0f
    }
}