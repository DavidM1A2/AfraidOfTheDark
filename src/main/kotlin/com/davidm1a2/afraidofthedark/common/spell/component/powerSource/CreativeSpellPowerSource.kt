package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class representing the creative power source
 */
class CreativeSpellPowerSource : AOTDSpellPowerSource<Unit>(ResourceLocation(Constants.MOD_ID, "creative"), ModResearches.THE_JOURNEY_BEGINS) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        return if (environment.vitaeAvailable >= spell.getCost()) {
            SpellCastResult.success()
        } else {
            SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        // Need to check "is EnchantedFrogEntity" for backward compatibility :(
        return if ((entity is PlayerEntity && entity.isCreative) || entity is EnchantedFrogEntity) {
            CastEnvironment.infiniteVitae(Unit)
        } else {
            CastEnvironment.noVitae(Unit)
        }
    }

    override fun getSourceSpecificCost(vitae: Double): Double {
        return vitae.round(1)
    }

    override fun shouldShowInSpellEditor(player: PlayerEntity): Boolean {
        if (!player.isCreative) {
            return false
        }

        return super.shouldShowInSpellEditor(player)
    }
}