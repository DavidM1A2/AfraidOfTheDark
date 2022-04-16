package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation

class SpellScrollPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "spell_scroll"), ModResearches.THE_JOURNEY_BEGINS) {
    override fun cast(entity: Entity, spell: Spell): SpellCastResult {
        return SpellCastResult.success()
    }

    override fun getSourceSpecificCost(rawCost: Double): Number {
        return rawCost.round(1)
    }

    override fun shouldShowInSpellEditor(player: PlayerEntity): Boolean {
        return true // TEMP
    }
}