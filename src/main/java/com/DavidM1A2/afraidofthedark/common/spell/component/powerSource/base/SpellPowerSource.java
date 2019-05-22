package com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Base class for all spell power sources
 */
public abstract class SpellPowerSource extends SpellComponent<SpellPowerSource>
{
    /**
     * Attempts to cast a given spell. Returns true if the power was consumed to cast the spell. Returns false
     * if not enough power was available to cast the spell
     *
     * @param stateNBT The NBT data associated with this spell power source instance
     * @param spell the spell to attempt to cast
     * @return true if the power was consumed to cast the spell, false otherwise
     */
    public abstract boolean consumePowerToCast(NBTTagCompound stateNBT, Spell spell);

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @param stateNBT The NBT data associated with this spell power source instance
     * @return A string describing why the power source doesn't have enough energy
     */
    public abstract String getNotEnoughPowerMessage(NBTTagCompound stateNBT);
}
