package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.core.AOTDShieldItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ShieldHandler {
    @SubscribeEvent
    fun onLivingAttackEvent(event: LivingAttackEvent) {
        val entity = event.entity
        val dmgSource = event.source
        val amount = event.amount

        if (amount <= 0.0f || entity !is PlayerEntity) return

        val useItem = entity.useItem.item

        if (useItem !is AOTDShieldItem) return

        if (isBlocked(entity, dmgSource)) useItem.onBlock(entity, dmgSource)
    }

    private fun isBlocked(playerEntity: PlayerEntity, source: DamageSource): Boolean {
        if (playerEntity.isBlocking) {
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
}