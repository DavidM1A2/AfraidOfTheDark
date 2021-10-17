package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation

/**
 * Class representing the experience source
 */
class HealthSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "health")) {
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
            it.hurt(DamageSource.OUT_OF_WORLD, hpCost.toFloat())
        }
    }

    companion object {
        // The number of units each hp supplies
        private const val UNIT_COST_PER_HP = 2.0
    }
}