package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class CorePowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "core")) {
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entity The entity that is casting the spell
     * @param spell        The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        return (entity as? PlayerEntity)?.let {
            val hpCost = spell.getCost() / UNIT_COST_PER_HP
            return hpCost <= (it.health + 20)   // They can use 10 "deficit" hearts (i.e. they can kill themselves :P)
        } ?: false
    }

    /**
     * Does nothing, creative power sources don't use energy
     *
     * @param entity The entity that is casting the spell
     * @param spell        the spell to attempt to cast
     */
    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        (entity as? PlayerEntity)?.let {
            val hpCost = spell.getCost() / UNIT_COST_PER_HP
            it.attackEntityFrom(DamageSource.OUT_OF_WORLD, hpCost.toFloat())
        }
    }

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    override fun getCostDescription(): String {
        return "1hp for every $UNIT_COST_PER_HP units of spell cost"
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    override fun getUnlocalizedOutOfPowerMsg(): String {
        return "message.afraidofthedark.spell.power_source.health.invalid_msg"
    }

    companion object {
        // The number of units each hp supplies
        private const val UNIT_COST_PER_HP = 2.0
    }
}