package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation

/**
 * Class representing the creative power source
 */
class CreativeSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "creative")) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        return (entity as? PlayerEntity)?.isCreative ?: true
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
    }

    override fun getSourceSpecificCost(rawCost: Double): Double {
        return rawCost.round(1)
    }
}