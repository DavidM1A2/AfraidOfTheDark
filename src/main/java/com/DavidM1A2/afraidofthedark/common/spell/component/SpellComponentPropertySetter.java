package com.DavidM1A2.afraidofthedark.common.spell.component;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Utility interface containing a method with the logic to set a property in an NBT tag compound
 */
public interface SpellComponentPropertySetter
{
    /**
     * A setter function used to set the new value of the property, will return null if
     * the value was set successfully or a string containing error information if something
     * went wrong. If null is passed in as the new value the default value should be set instead.
     *
     * @param newValue The new value of the property, nor null if the default value should be set
     * @param stateNBT The state to set the value in
     * @return An error message if something went wrong or null if the property was set successfully
     */
    String setProperty(String newValue, NBTTagCompound stateNBT);
}
