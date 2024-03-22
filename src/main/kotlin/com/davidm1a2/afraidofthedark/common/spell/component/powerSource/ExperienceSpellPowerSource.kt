package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.util.text.TranslatableComponent
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class ExperienceSpellPowerSource : AOTDSpellPowerSource<Unit>("experience", ModResearches.WISDOM) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        if (entity !is Player) {
            return SpellCastResult.failure(TranslatableComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslatableComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val xpCost = ceil(spell.getCost() / VITAE_PER_XP).toInt()
        entity.giveExperiencePoints(-xpCost)

        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        if (entity !is Player) {
            return CastEnvironment.noVitae(Unit)
        }

        return CastEnvironment.withVitae(entity.totalExperience * VITAE_PER_XP, Unit)
    }

    override fun getSourceSpecificCost(vitae: Double): Int {
        return ceil(vitae / VITAE_PER_XP).toInt()
    }

    companion object {
        // The number of units each xp supplies
        private const val VITAE_PER_XP = 1.0
    }
}