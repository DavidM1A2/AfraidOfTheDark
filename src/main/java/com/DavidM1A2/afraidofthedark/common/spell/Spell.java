package com.DavidM1A2.afraidofthedark.common.spell;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

/**
 * Class representing a spell instance created by a player
 */
public class Spell implements INBTSerializable<NBTTagCompound>
{
    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell's data
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        return nbt;
    }

    /**
     * Reads in the contents of the NBT into the object
     *
     * @param nbt The NBT compound to read from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {

    }

    public UUID getId()
    {
        return UUID.randomUUID();
    }
}
