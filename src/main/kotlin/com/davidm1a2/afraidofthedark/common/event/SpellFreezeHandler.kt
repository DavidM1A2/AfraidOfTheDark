package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

/**
 * Handles the on server tick to update any existing spell freeze effects
 */
class SpellFreezeHandler {
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
            // Dead players don't have capabilities
            if (entityPlayer.isAlive) {
                val playerFreezeData = entityPlayer.getSpellFreezeData()

                // Ensure there's at least 1 freeze tick remaining
                if (playerFreezeData.freezeTicks > 0) {
                    // Reduce the freeze ticks by 1
                    val newFreezeTicks = playerFreezeData.freezeTicks - 1
                    playerFreezeData.freezeTicks = newFreezeTicks

                    // If no freeze ticks are left tell the client
                    if (newFreezeTicks == 0) {
                        playerFreezeData.sync(entityPlayer)
                    }

                    val freezePosition = playerFreezeData.freezePosition!!

                    // Freeze the player's location
                    (entityPlayer as ServerPlayerEntity).connection.teleport(
                        freezePosition.x,
                        freezePosition.y,
                        freezePosition.z,
                        playerFreezeData.freezeYaw,
                        playerFreezeData.freezePitch
                    )
                }
            }
        }
    }

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