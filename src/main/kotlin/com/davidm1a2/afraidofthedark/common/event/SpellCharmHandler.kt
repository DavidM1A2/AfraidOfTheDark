package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellCharmData
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import kotlin.math.asin
import kotlin.math.atan2

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
                    val charmingEntity = (event.player.world as? ServerWorld)?.getEntityByUuid(charmingEntityId!!)

                    // If the player is non-null set the player's facing
                    if (charmingEntity != null) {
                        // A player cant charm themselves
                        if (entityPlayer.uniqueID != charmingEntityId) {
                            val playerEyePosition = entityPlayer.getEyePosition(1.0f)
                            // Compute the vector from the charming entity to the charmed entity
                            val direction = charmingEntity.getEyePosition(1.0f)
                                .subtract(playerEyePosition.x, playerEyePosition.y, playerEyePosition.z)
                                .normalize()

                            // Convert 3d direction vector to pitch and yaw. Yes, coerceIn() is required. Due to java's float preceision not being very high,
                            // direction vectors might be slightly outside of the [-1, 1] bounds, eg: -1.0000000118067964
                            val xDirection = direction.x.coerceIn(-1.0, 1.0)
                            val zDirection = direction.z.coerceIn(-1.0, 1.0)
                            val yDirection = direction.y.coerceIn(-1.0, 1.0)
                            val yaw = (-atan2(xDirection, zDirection) * 180 / Math.PI).toFloat()
                            val pitch = (-asin(yDirection) * 180 / Math.PI).toFloat()

                            // Set the player's look to be at the charming entity
                            (entityPlayer as ServerPlayerEntity).connection.setPlayerLocation(
                                // TODO: In forge 1.15, this should not be needed. Use 0s and make the TP relative
                                entityPlayer.posX,
                                entityPlayer.posY,
                                entityPlayer.posZ,
                                yaw,
                                pitch
                            )
                        }
                    }
                }
            }
        }
    }
}