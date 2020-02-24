package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile
import net.minecraft.entity.Entity
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.EntityDamageSourceIndirect

/**
 * Class containing definitions for AOTD damage sources
 */
object ModDamageSources {
    // Constant names for the damage sources
    const val SILVER_DAMAGE = "silver_damage"
    const val PLASMA_BALL = "plasma_ball"

    /**
     * Returns a silver damage source given an entity to cause the damage
     *
     * @param entity The entity hitting
     * @return The silver damage source
     */
    fun getSilverDamage(entity: Entity?): DamageSource {
        return EntityDamageSource(SILVER_DAMAGE, entity)
    }

    /**
     * Returns a plasma ball damage source given an entity source and target
     *
     * @param source         The projectile that hit the entity
     * @param indirectSource The drone that fired the shot
     * @return The damage source to apply
     */
    fun causePlasmaBallDamage(
        source: EntitySplinterDroneProjectile,
        indirectSource: EntitySplinterDrone?
    ): DamageSource {
        return EntityDamageSourceIndirect(PLASMA_BALL, source, indirectSource).setProjectile()
    }
}