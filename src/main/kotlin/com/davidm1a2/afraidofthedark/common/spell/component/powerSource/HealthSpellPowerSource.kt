package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class HealthSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "health"), ModResearches.BLOOD_MAGIC) {
    override fun cast(entity: Entity, spell: Spell): SpellCastResult {
        if (entity !is LivingEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val hpCost = spell.getCost() / UNIT_COST_PER_HP
        // They can use 10 "deficit" hearts (i.e. they can kill themselves :P)
        if (hpCost > (entity.health + 20)) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        entity.hurt(DamageSource.OUT_OF_WORLD, hpCost.toFloat())

        return SpellCastResult.success()
    }

    override fun getSourceSpecificCost(rawCost: Double): Double {
        return ceil(rawCost / UNIT_COST_PER_HP) / 2.0
    }

    companion object {
        // The number of units each hp supplies
        private const val UNIT_COST_PER_HP = 5.0
    }
}