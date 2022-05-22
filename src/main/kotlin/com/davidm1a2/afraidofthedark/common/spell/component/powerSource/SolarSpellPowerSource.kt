package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

class SolarSpellPowerSource : AOTDSpellPowerSource<Unit>(ResourceLocation(Constants.MOD_ID, "solar"), ModResearches.MAGIC_MASTERY) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        if (entity !is PlayerEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val solarData = entity.getSpellSolarData()
        solarData.vitae = solarData.vitae - spell.getCost()
        solarData.sync(entity as ServerPlayerEntity)

        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        if (entity !is PlayerEntity) {
            return CastEnvironment.noVitae(Unit)
        }

        val solarData = entity.getSpellSolarData()
        val canSeeSky = entity.level.canSeeSky(entity.blockPosition())
        if (!canSeeSky) {
            return CastEnvironment.withVitae(0.0, solarData.getMaxVitae(entity.level), Unit)
        }

        return CastEnvironment.withVitae(solarData.vitae, solarData.getMaxVitae(entity.level), Unit)
    }

    override fun getSourceSpecificCost(vitae: Double): Number {
        return vitae.round(1)
    }
}