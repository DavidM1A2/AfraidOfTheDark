package com.DavidM1A2.afraidofthedark.common.spell.component.powerSource;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Class representing the creative power source
 */
public class SpellPowerSourceCreative extends AOTDSpellPowerSource
{
    /**
     * Constructor sets the power source's name
     */
    public SpellPowerSourceCreative()
    {
        super("creative");
    }

    /**
     * Attempts to cast a given spell. Returns true if the power was consumed to cast the spell. Returns false
     * if not enough power was available to cast the spell
     *
     * @param stateNBT The NBT data associated with this spell power source instance
     * @param spell the spell to attempt to cast
     * @return true, since the creative mode power source always works
     */
    public boolean consumePowerToCast(NBTTagCompound stateNBT, Spell spell)
    {
        return true;
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @param stateNBT The NBT data associated with this spell power source instance
     * @return A string describing why the power source doesn't have enough energy
     */
    @Override
    public String getNotEnoughPowerMessage(NBTTagCompound stateNBT)
    {
        return "How did you use creative with infinite energy and not cast the spell?";
    }
}
