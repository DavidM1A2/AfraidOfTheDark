package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent

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
    fun onPlayerTick(event: PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
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
                (entityPlayer as EntityPlayerMP).connection.setPlayerLocation(
                    freezePosition.x,
                    freezePosition.y,
                    freezePosition.z,
                    playerFreezeData.getFreezeYaw(),
                    playerFreezeData.getFreezePitch()
                )
            }
        }
    }

    /**
     * Called on the client side to block all movement input
     *
     * @param event The event to modify
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun onInputUpdateEvent(event: InputUpdateEvent) {
        val playerFreezeData = event.entityPlayer.getSpellFreezeData()

        // If the player is frozen block all movement
        if (playerFreezeData.freezeTicks > 0) {
            val input = event.movementInput
            input.backKeyDown = false
            input.forwardKeyDown = false
            input.jump = false
            input.leftKeyDown = false
            input.rightKeyDown = false
            input.moveStrafe = 0f
            input.moveForward = 0f
            input.sneak = false
        }
    }
}