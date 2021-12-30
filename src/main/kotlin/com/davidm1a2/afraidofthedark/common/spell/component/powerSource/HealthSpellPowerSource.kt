package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class HealthSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "health"), ModResearches.BLOOD_MAGIC) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        return (entity as? PlayerEntity)?.let {
            val hpCost = spell.getCost() / UNIT_COST_PER_HP
            return hpCost <= (it.health + 20)   // They can use 10 "deficit" hearts (i.e. they can kill themselves :P)
        } ?: false
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        (entity as? PlayerEntity)?.let {
            val hpCost = spell.getCost() / UNIT_COST_PER_HP
            it.hurt(DamageSource.OUT_OF_WORLD, hpCost.toFloat())
        }
    }

    override fun getSourceSpecificCost(rawCost: Double): Double {
        return ceil(rawCost / UNIT_COST_PER_HP) / 2.0
    }

    companion object {
        // The number of units each hp supplies
        private const val UNIT_COST_PER_HP = 3.0
    }
}