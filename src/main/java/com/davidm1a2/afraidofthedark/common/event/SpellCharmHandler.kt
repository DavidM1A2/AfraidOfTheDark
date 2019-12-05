package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.play.server.SPacketPlayerPosLook.EnumFlags
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.asin
import kotlin.math.atan2

/**
 * Handles events that let the player be charmed
 */
class SpellCharmHandler
{
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    @SideOnly(Side.SERVER)
    fun onPlayerTick(event: PlayerTickEvent)
    {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == Side.SERVER)
        {
            val entityPlayer = event.player
            val playerCharmData = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA, null)!!

            // Ensure there's at least 1 charm tick remaining
            if (playerCharmData.charmTicks > 0)
            {
                // Reduce the charm ticks by 1
                playerCharmData.charmTicks = playerCharmData.charmTicks - 1

                // Force the player to look at the entity
                val charmingEntityId = playerCharmData.charmingEntityId
                val charmingEntity = FMLCommonHandler.instance().minecraftServerInstance.getEntityFromUuid(charmingEntityId!!)

                // If the player is non-null set the player's facing
                if (charmingEntity != null)
                {
                    // A player cant charm themselves
                    if (entityPlayer.persistentID != charmingEntityId)
                    {
                        // Compute the vector from the charming entity to the charmed entity
                        val direction = Vec3d(charmingEntity.posX, charmingEntity.posY, charmingEntity.posZ).subtract(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ).normalize()
                        // Convert 3d direction vector to pitch and yaw
                        val yaw = (-atan2(direction.x, direction.z) * 180 / Math.PI).toFloat()
                        val pitch = (-asin(direction.y) * 180 / Math.PI).toFloat()
                        // Set the player's look to be at the charming entity
                        (entityPlayer as EntityPlayerMP).connection.setPlayerLocation(
                            0.0, 0.0, 0.0,
                            yaw,
                            pitch,
                            setOf(EnumFlags.X, EnumFlags.Y, EnumFlags.Z)
                        )
                    }
                }
            }
        }
    }
}