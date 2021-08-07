package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellCharmData
import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

/**
 * Handles events that let the player be charmed
 */
class SpellCharmHandler {
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
                val playerCharmData = entityPlayer.getSpellCharmData()

                // Ensure there's at least 1 charm tick remaining
                if (playerCharmData.charmTicks > 0) {
                    // Reduce the charm ticks by 1
                    playerCharmData.charmTicks = playerCharmData.charmTicks - 1

                    // Force the player to look at the entity
                    val charmingEntityId = playerCharmData.charmingEntityId
                    val charmingEntity = (event.player.level as? ServerWorld)?.getEntity(charmingEntityId!!)

                    // If the player is non-null set the player's facing
                    if (charmingEntity != null) {
                        // A player cant charm themselves
                        if (entityPlayer.uuid != charmingEntityId) {
                            // Set the player's look to be at the charming entity
                            (entityPlayer as ServerPlayerEntity).lookAt(EntityAnchorArgument.Type.EYES, charmingEntity, EntityAnchorArgument.Type.EYES)
                        }
                    }
                }
            }
        }
    }
}