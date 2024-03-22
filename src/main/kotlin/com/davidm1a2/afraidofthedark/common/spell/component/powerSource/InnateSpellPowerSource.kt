package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellInnateData
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.entity.player.ServerPlayer
import net.minecraft.util.text.TranslationTextComponent

class InnateSpellPowerSource : AOTDSpellPowerSource<Unit>("innate", ModResearches.THE_JOURNEY_BEGINS) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        if (entity !is Player) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val innateData = entity.getSpellInnateData()
        innateData.vitae = innateData.vitae - spell.getCost()
        innateData.sync(entity as ServerPlayer)

        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        if (entity !is Player) {
            return CastEnvironment.noVitae(Unit)
        }

        val innateData = entity.getSpellInnateData()
        return CastEnvironment.withVitae(innateData.vitae, innateData.getMaxVitae(entity), Unit)
    }

    override fun getSourceSpecificCost(vitae: Double): Number {
        return vitae.round(1)
    }
}