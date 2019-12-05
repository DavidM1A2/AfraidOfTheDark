package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Handles the on server tick to update any existing spell freeze effects
 */
class SpellFreezeHandler
{
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent)
    {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == Side.SERVER)
        {
            val entityPlayer = event.player
            val playerFreezeData = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null)!!

            // Ensure there's at least 1 freeze tick remaining
            if (playerFreezeData.freezeTicks > 0)
            {
                // Reduce the freeze ticks by 1
                val newFreezeTicks = playerFreezeData.freezeTicks - 1
                playerFreezeData.freezeTicks = newFreezeTicks

                // If no freeze ticks are left tell the client
                if (newFreezeTicks == 0)
                {
                    playerFreezeData.sync(entityPlayer)
                }

                // Freeze the player's location
                val freezePosition = playerFreezeData.freezePosition!!
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
    @SideOnly(Side.CLIENT)
    fun onInputUpdateEvent(event: InputUpdateEvent)
    {
        val playerFreezeData = event.entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null)!!
        // If the player is frozen block all movement
        if (playerFreezeData.freezeTicks > 0)
        {
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