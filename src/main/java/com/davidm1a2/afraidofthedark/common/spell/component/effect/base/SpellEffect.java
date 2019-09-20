package com.davidm1a2.afraidofthedark.common.spell.component.effect.base;

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all spell effects
 */
public abstract class SpellEffect extends SpellComponent
{
    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    public abstract double getCost();

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    public abstract void procEffect(DeliveryTransitionState state);

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public abstract SpellEffectEntry getEntryRegistryType();

    /**
     * Utility function to create a spell effect from NBT
     *
     * @param nbt The NBT to get the effect information from
     * @return The spell effect instance from NBT
     */
    public static SpellEffect createFromNBT(NBTTagCompound nbt)
    {
        // Figure out the type of delivery method that this NBT represents
        String effectTypeId = nbt.getString(NBT_TYPE_ID);
        // Use our registry to create a new instance of this type
        SpellEffect effect = ModRegistries.SPELL_EFFECTS.getValue(new ResourceLocation(effectTypeId)).newInstance();
        // Load in the effect's state from NBT
        effect.deserializeNBT(nbt);
        // Return the effect
        return effect;
    }
}
