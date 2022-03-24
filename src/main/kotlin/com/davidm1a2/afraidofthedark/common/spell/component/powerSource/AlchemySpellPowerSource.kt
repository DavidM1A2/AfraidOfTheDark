package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
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
class AlchemySpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "alchemy"), ModResearches.ALCHEMY) {
    override fun cast(entity: Entity, spell: Spell): SpellCastResult {
        if (entity !is PlayerEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val inventory = entity.inventory.items + entity.inventory.offhand
        var goldCount = 0
        for (stack in inventory) {
            if (stack.item == Items.GOLD_INGOT) {
                goldCount += stack.count
            }
        }

        if (goldCount * UNIT_COST_PER_GOLD < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        var costRemaining = spell.getCost()
        for (stack in inventory) {
            if (stack.item == Items.GOLD_INGOT) {
                while (costRemaining > 0.0 && stack.count > 0) {
                    costRemaining -= UNIT_COST_PER_GOLD
                    stack.count--
                }
            }
            if (costRemaining <= 0.0) break
        }
        return SpellCastResult.success()
    }

    override fun getSourceSpecificCost(rawCost: Double): Number {
        return ceil(rawCost / UNIT_COST_PER_GOLD).toInt()
    }

    companion object {
        // The number of units each gold ingot
        private const val UNIT_COST_PER_GOLD = 10.0
    }
}