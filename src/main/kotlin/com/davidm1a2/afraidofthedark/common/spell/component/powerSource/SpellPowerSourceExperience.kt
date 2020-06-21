package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class SpellPowerSourceExperience : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "experience")) {
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entity The entity that is casting the spell
     * @param spell        The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        return (entity as? EntityPlayer)?.let {
            val xpLevelCost = ceil(spell.getCost() / UNIT_COST_PER_LEVEL).toInt()
            return xpLevelCost <= it.experienceLevel
        } ?: false
    }

    /**
     * Does nothing, creative power sources don't use energy
     *
     * @param entity The entity that is casting the spell
     * @param spell        the spell to attempt to cast
     */
    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        (entity as? EntityPlayer)?.let {
            val xpLevelCost = ceil(spell.getCost() / UNIT_COST_PER_LEVEL).toInt()
            it.addExperienceLevel(-xpLevelCost)
        }
    }

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    override fun getCostDescription(): String {
        return "1xp level for every $UNIT_COST_PER_LEVEL units of spell cost"
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    override fun getUnlocalizedOutOfPowerMsg(): String {
        return LocalizationConstants.Spell.POWER_SOURCE_EXPERIENCE_INVALID_MSG
    }

    companion object {
        // The number of units each xp level supplies
        private const val UNIT_COST_PER_LEVEL = 5.0
    }
}