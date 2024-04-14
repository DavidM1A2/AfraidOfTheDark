package com.davidm1a2.afraidofthedark.common.utility.damagesource

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.EntityDamageSource
import net.minecraft.world.damagesource.IndirectEntityDamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

enum class AOTDDamageType {
    DARK,
    SILVER,
    SORCERY;

    companion object {
        fun resolve(damageSource: DamageSource?): AOTDDamageType? {
            if (damageSource is IndirectEntityDamageSource) {
                return resolve(damageSource.directEntity)
            }
            if (damageSource is EntityDamageSource) {
                return resolve(damageSource.entity)
            }

            return null
        }

        fun resolve(entity: Entity?): AOTDDamageType? {
            if (entity is LivingEntity) {
                val heldItem = entity.mainHandItem.item
                if (heldItem is IDoesAOTDDamageType) {
                    return heldItem.type
                }
            }
            if (entity is IDoesAOTDDamageType) {
                return entity.type
            }

            return null
        }
    }
}