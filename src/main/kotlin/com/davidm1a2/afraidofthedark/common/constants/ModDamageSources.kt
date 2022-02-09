package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AstralSilverDamageSource
import com.davidm1a2.afraidofthedark.common.utility.damagesource.PlasmaBallDamageSource
import com.davidm1a2.afraidofthedark.common.utility.damagesource.SpellDamageSource
import net.minecraft.entity.Entity
import net.minecraft.util.DamageSource

/**
 * Class containing definitions for AOTD damage sources
 */
object ModDamageSources {
    fun getSilverDamage(entity: Entity?): DamageSource {
        return AstralSilverDamageSource(entity)
    }

    fun getPlasmaBallDamage(
        source: SplinterDroneProjectileEntity,
        indirectSource: SplinterDroneEntity?
    ): DamageSource {
        return PlasmaBallDamageSource(source, indirectSource)
    }

    fun getSpellDamage(spellDamageState: DeliveryTransitionState): DamageSource {
        return SpellDamageSource(spellDamageState)
    }
}