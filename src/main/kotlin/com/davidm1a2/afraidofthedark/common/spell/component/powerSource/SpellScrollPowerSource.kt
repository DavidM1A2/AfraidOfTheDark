package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity

class SpellScrollPowerSource : AOTDSpellPowerSource<Unit>("spell_scroll", ModResearches.THE_JOURNEY_BEGINS) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        return CastEnvironment.infiniteVitae(Unit)
    }

    override fun getSourceSpecificCost(vitae: Double): Number {
        return vitae.round(1)
    }

    override fun shouldShowInSpellEditor(player: PlayerEntity): Boolean {
        return false
    }
}