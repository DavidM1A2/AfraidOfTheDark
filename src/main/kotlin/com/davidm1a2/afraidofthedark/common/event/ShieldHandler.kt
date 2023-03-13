package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.core.AOTDShieldItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.math.max

class ShieldHandler {
    @SubscribeEvent
    fun onLivingAttackEvent(event: LivingAttackEvent) {
        val entity = event.entity
        val dmgSource = event.source
        val amount = event.amount
        val dmgSourceEntity = dmgSource.entity

        if (entity is PlayerEntity) {
            if (amount > 0.0f && isDamageSourceBlocked(entity, dmgSource)) {
                if (dmgSourceEntity != null) {
                    dmgSourceEntity.remainingFireTicks = max(dmgSourceEntity.remainingFireTicks, 40)

                    val direction = dmgSourceEntity.position()
                        .subtract(entity.position())
                        .normalize()
                        .scale(KNOCKBACK_STRENGTH)

                    // Move the entity away from the player
                    dmgSourceEntity.push(
                        direction.x,
                        direction.y,
                        direction.z
                    )
                }
            }
        }
    }

    private fun isDamageSourceBlocked(playerEntity: PlayerEntity, source: DamageSource): Boolean {
        if (playerEntity.isBlocking && playerEntity.useItem.item is AOTDShieldItem) {
            val vector3d2 = source.sourcePosition
            if (vector3d2 != null) {
                val vector3d: Vector3d = playerEntity.getViewVector(1.0f)
                var vector3d1 = vector3d2.vectorTo(playerEntity.position()).normalize()
                vector3d1 = Vector3d(vector3d1.x, 0.0, vector3d1.z)
                if (vector3d1.dot(vector3d) < 0.0) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        // How much strength the armor knocks back enemies that attack you. It's roughly the number of blocks to push
        private const val KNOCKBACK_STRENGTH = 2.0
    }
}