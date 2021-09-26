package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ClientSpellFreezeHandler {
    /**
     * Called on the client side to block all movement input
     *
     * @param event The event to modify
     */
    @SubscribeEvent
    fun onInputUpdateEvent(event: InputUpdateEvent) {
        // Dead players don't have capabilities
        if (event.player.isAlive) {
            // Only freeze alive players
            val playerFreezeData = event.player.getSpellFreezeData()

            // If the player is frozen block all movement
            if (playerFreezeData.freezeTicks > 0) {
                val input = event.movementInput
                input.down = false
                input.up = false
                input.jumping = false
                input.left = false
                input.right = false
                input.leftImpulse = 0f
                input.forwardImpulse = 0f
                input.shiftKeyDown = false
            }
        }
    }
}
