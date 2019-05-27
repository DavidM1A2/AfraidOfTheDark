package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all spell delivery methods
 */
public abstract class SpellDeliveryMethod extends SpellComponent
{
    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    public abstract double getCost();

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    public abstract double getStageCostMultiplier();

    /**
     * Utility function to create a spell delivery method from NBT
     *
     * @param nbt The NBT to get the delivery method information from
     * @return The spell delivery method instance from NBT
     */
    public static SpellDeliveryMethod createFromNBT(NBTTagCompound nbt)
    {
        // Figure out the type of delivery method that this NBT represents
        String deliveryMethodTypeId = nbt.getString(NBT_TYPE_ID);
        // Use our registry to create a new instance of this type
        SpellDeliveryMethod deliveryMethod = ModRegistries.SPELL_DELIVERY_METHODS.getValue(new ResourceLocation(deliveryMethodTypeId)).newInstance();
        // Load in the delivery method's state from NBT
        deliveryMethod.deserializeNBT(nbt);
        // Return the delivery method
        return deliveryMethod;
    }
}
