package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import kotlin.math.ceil

/**
 * Class representing the alchemy source
 */
class AlchemySpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "alchemy"), ModResearches.ALCHEMY) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.items + entity.inventory.offhand
            var goldCount = 0
            for (stack in inventory) {
                if (stack.item == Items.GOLD_INGOT) {
                    goldCount += stack.count
                }
            }
            return goldCount * UNIT_COST_PER_GOLD > spell.getCost()
        }
        return false
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.items + entity.inventory.offhand
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
        }
    }

    override fun getSourceSpecificCost(rawCost: Double): Double {
        return ceil(rawCost / UNIT_COST_PER_GOLD)
    }

    companion object {
        // The number of units each gold ingot
        private const val UNIT_COST_PER_GOLD = 5.0
    }
}