package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellLunarData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation

class LunarSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "lunar"), ModResearches.THE_JOURNEY_BEGINS) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity !is PlayerEntity) {
            return false
        }

        val lunarData = entity.getSpellLunarData()
        return lunarData.vitae >= spell.getCost()
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity !is PlayerEntity) {
            return
        }

        val lunarData = entity.getSpellLunarData()
        lunarData.vitae = lunarData.vitae - spell.getCost()
        lunarData.sync(entity as ServerPlayerEntity)
    }

    override fun getSourceSpecificCost(rawCost: Double): Number {
        return rawCost.round(1)
    }
}