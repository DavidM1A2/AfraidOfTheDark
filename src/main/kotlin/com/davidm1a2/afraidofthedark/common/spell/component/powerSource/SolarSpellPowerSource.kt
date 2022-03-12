package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation

class SolarSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "solar"), ModResearches.THE_JOURNEY_BEGINS) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity !is PlayerEntity) {
            return false
        }

        val canSeeSky = entity.level.canSeeSky(entity.blockPosition())
        if (!canSeeSky) {
            return false
        }

        val solarData = entity.getSpellSolarData()
        return solarData.vitae >= spell.getCost()
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity !is PlayerEntity) {
            return
        }

        val solarData = entity.getSpellSolarData()
        solarData.vitae = solarData.vitae - spell.getCost()
        solarData.sync(entity as ServerPlayerEntity)
    }

    override fun getSourceSpecificCost(rawCost: Double): Number {
        return rawCost.round(1)
    }
}