package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.item.FlaskOfSoulsItem
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Class representing the alchemy source
 */
class AlchemySpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "alchemy")) {
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entity The entity that is casting the spell
     * @param spell        The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.mainInventory + entity.inventory.offHandInventory
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

    /**
     * Consume gold ingots in accordance with the spell cost
     *
     * @param entity The entity that is casting the spell
     * @param spell        the spell to attempt to cast
     */
    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.mainInventory + entity.inventory.offHandInventory
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

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    override fun getCostDescription(): String {
        return "Transmute gold ingots into magic essence at a rate of $UNIT_COST_PER_GOLD spell power per ingot"
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    override fun getUnlocalizedOutOfPowerMsg(): String {
        return "message.afraidofthedark.spell.power_source.alchemy.invalid_msg"
    }

    companion object {
        // The number of units each gold ingot
        private const val UNIT_COST_PER_GOLD = 5.0
    }
}