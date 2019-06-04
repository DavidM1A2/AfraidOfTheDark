package com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all spell power sources
 */
public abstract class SpellPowerSource extends SpellComponent
{
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entityPlayer The player that is casting the spell
     * @param spell The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    public abstract boolean canCast(EntityPlayer entityPlayer, Spell spell);

    /**
     * Consumes power to cast a given spell. canCast must return true first to ensure there is
     * enough power to cast the spell
     *
     * @param entityPlayer The player that is casting the spell
     * @param spell the spell to attempt to cast
     */
    public abstract void consumePowerToCast(EntityPlayer entityPlayer, Spell spell);

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    public abstract String getUnlocalizedOutOfPowerMsg();

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    public abstract String getCostDescription();

    /**
     * Should get the SpellPowerSourceEntry registry's type
     *
     * @return The registry entry that this power source was built with, used for deserialization
     */
    @Override
    public abstract SpellPowerSourceEntry getEntryRegistryType();

    /**
     * Utility function to create a spell power source from NBT
     *
     * @param nbt The NBT to get the power source information from
     * @return The spell power source instance from NBT
     */
    public static SpellPowerSource createFromNBT(NBTTagCompound nbt)
    {
        // Figure out the type of power source that this NBT represents
        String powerSourceTypeId = nbt.getString(NBT_TYPE_ID);
        // Use our registry to create a new instance of this type
        SpellPowerSource powerSource = ModRegistries.SPELL_POWER_SOURCES.getValue(new ResourceLocation(powerSourceTypeId)).newInstance();
        // Load in the power source's state from NBT
        powerSource.deserializeNBT(nbt);
        // Return the power source
        return powerSource;
    }
}
