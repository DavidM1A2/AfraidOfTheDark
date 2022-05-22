package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

/**
 * Class representing the alchemy source
 */
class AlchemySpellPowerSource : AOTDSpellPowerSource<Unit>(ResourceLocation(Constants.MOD_ID, "alchemy"), ModResearches.ALCHEMY) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        if (entity !is PlayerEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        var costRemaining = spell.getCost()
        val inventory = entity.inventory.items + entity.inventory.offhand
        for (stack in inventory) {
            if (stack.item == Items.GOLD_INGOT) {
                while (costRemaining > 0.0 && stack.count > 0) {
                    costRemaining -= VITAE_PER_GOLD
                    stack.count--
                }
            }
            if (costRemaining <= 0.0) break
        }
        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        if (entity !is PlayerEntity) {
            return CastEnvironment.noVitae(Unit)
        }

        val inventory = entity.inventory.items + entity.inventory.offhand
        var goldCount = 0
        for (stack in inventory) {
            if (stack.item == Items.GOLD_INGOT) {
                goldCount += stack.count
            }
        }

        return CastEnvironment.withVitae(goldCount * VITAE_PER_GOLD, inventory.size * 64 * VITAE_PER_GOLD, Unit)
    }

    override fun getSourceSpecificCost(vitae: Double): Number {
        return ceil(vitae / VITAE_PER_GOLD).toInt()
    }

    companion object {
        // The number of vitae each gold ingot
        private const val VITAE_PER_GOLD = 10.0
    }
}