package com.DavidM1A2.afraidofthedark.common.spell.component.powerSource;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellPowerSources;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Class representing the experience source
 */
public class SpellPowerSourceExperience extends AOTDSpellPowerSource
{
    // The number of units each xp level supplies
    private static final double UNIT_COST_PER_LEVEL = 5;

    /**
     * Constructor just calls super
     */
    public SpellPowerSourceExperience()
    {
        super();
    }

    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entityPlayer The player that is casting the spell
     * @param spell        The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    @Override
    public boolean canCast(EntityPlayer entityPlayer, Spell spell)
    {
        int xpLevelCost = (int) Math.ceil(spell.getCost() / UNIT_COST_PER_LEVEL);
        return xpLevelCost <= entityPlayer.experienceLevel;
    }

    /**
     * Does nothing, creative power sources don't use energy
     *
     * @param entityPlayer The player that is casting the spell
     * @param spell        the spell to attempt to cast
     */
    public void consumePowerToCast(EntityPlayer entityPlayer, Spell spell)
    {
        int xpLevelCost = (int) Math.ceil(spell.getCost() / UNIT_COST_PER_LEVEL);
        entityPlayer.addExperienceLevel(-xpLevelCost);
    }

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    @Override
    public String getCostDescription()
    {
        return "1xp level for every " + UNIT_COST_PER_LEVEL + " units of spell cost";
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    @Override
    public String getUnlocalizedOutOfPowerMsg()
    {
        return "aotd.spell.power_source.experience.invalid_msg";
    }

    /**
     * Should get the SpellPowerSourceEntry registry's type
     *
     * @return The registry entry that this power source was built with, used for deserialization
     */
    @Override
    public SpellPowerSourceEntry getEntryRegistryType()
    {
        return ModSpellPowerSources.EXPERIENCE;
    }
}
