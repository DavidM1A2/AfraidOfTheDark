package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation

/**
 * Class representing the creative power source
 */
class SpellPowerSourceCreative : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "creative"))
{
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entityPlayer The player that is casting the spell
     * @param spell The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    override fun canCast(entityPlayer: EntityPlayer, spell: Spell): Boolean
    {
        return true
    }

    /**
     * Does nothing, creative power sources don't use energy
     *
     * @param entityPlayer The player that is casting the spell
     * @param spell the spell to attempt to cast
     */
    override fun consumePowerToCast(entityPlayer: EntityPlayer, spell: Spell)
    {
    }

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    override fun getCostDescription(): String
    {
        return "Unlimited power!"
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    override fun getUnlocalizedOutOfPowerMsg(): String
    {
        return "message.afraidofthedark:spell.power_source.creative.invalid_msg"
    }
}