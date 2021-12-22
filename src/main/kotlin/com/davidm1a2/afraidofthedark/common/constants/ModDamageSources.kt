package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import net.minecraft.entity.Entity
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.IndirectEntityDamageSource

/**
 * Class containing definitions for AOTD damage sources
 */
object ModDamageSources {
    // Constant names for the damage sources
    const val SILVER_DAMAGE = "silver_damage"
    const val PLASMA_BALL = "plasma_ball"
    const val SPELL_DAMAGE = "spell_damage"

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
        source: SplinterDroneProjectileEntity,
        indirectSource: SplinterDroneEntity?
    ): DamageSource {
        return IndirectEntityDamageSource(PLASMA_BALL, source, indirectSource).setProjectile()
    }

    fun getSpellDamage(spellCaster: Entity?): DamageSource {
        return EntityDamageSource(SPELL_DAMAGE, spellCaster)
    }
}