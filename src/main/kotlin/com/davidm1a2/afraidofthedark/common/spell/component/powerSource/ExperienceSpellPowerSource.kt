package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class ExperienceSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "experience"), ModResearches.WISDOM) {
    override fun cast(entity: Entity, spell: Spell): SpellCastResult {
        if (entity !is PlayerEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val xpCost = ceil(spell.getCost() / UNIT_COST_PER_XP).toInt()
        if (xpCost > entity.totalExperience) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        entity.giveExperiencePoints(-xpCost)

        return SpellCastResult.success()
    }

    override fun getSourceSpecificCost(rawCost: Double): Int {
        return ceil(rawCost / UNIT_COST_PER_XP).toInt()
    }

    companion object {
        // The number of units each xp supplies
        private const val UNIT_COST_PER_XP = 1.0
    }
}