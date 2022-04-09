package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

class SolarSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "solar"), ModResearches.MAGIC_MASTERY) {
    override fun cast(entity: Entity, spell: Spell): SpellCastResult {
        if (entity !is PlayerEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val solarData = entity.getSpellSolarData()
        if (solarData.vitae < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val canSeeSky = entity.level.canSeeSky(entity.blockPosition())
        if (!canSeeSky) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        solarData.vitae = solarData.vitae - spell.getCost()
        solarData.sync(entity as ServerPlayerEntity)

        return SpellCastResult.success()
    }

    override fun getSourceSpecificCost(rawCost: Double): Number {
        return rawCost.round(1)
    }
}