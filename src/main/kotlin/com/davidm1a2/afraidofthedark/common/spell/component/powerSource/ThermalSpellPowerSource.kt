package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellThermalData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation

class ThermalSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "thermal"), ModResearches.THE_JOURNEY_BEGINS) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity !is PlayerEntity) {
            return false
        }

        val thermalData = entity.getSpellThermalData()
        return thermalData.vitae >= spell.getCost()
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity !is PlayerEntity) {
            return
        }

        val thermalData = entity.getSpellThermalData()
        thermalData.vitae = thermalData.vitae - spell.getCost()
        thermalData.sync(entity as ServerPlayerEntity)
    }

    override fun getSourceSpecificCost(rawCost: Double): Number {
        return rawCost.round(1)
    }
}