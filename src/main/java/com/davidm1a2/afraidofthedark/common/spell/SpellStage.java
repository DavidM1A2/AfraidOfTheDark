package com.davidm1a2.afraidofthedark.common.spell;

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Class representing a stage in a spell
 */
public class SpellStage implements INBTSerializable<NBTTagCompound>
{
    // NBT Tag constants
    private static final String NBT_DELIVERY_METHOD = "delivery_method";
    private static final String NBT_EFFECT_BASE = "effect_";
    // The max number of effects per spell stage
    private static final Integer MAX_EFFECTS_PER_STAGE = 4;

    // The delivery method for this spell stage, can be null
    private SpellDeliveryMethod deliveryMethod;
    // A list of 4 effects for this spell stage, each can be null but the array can't be
    private SpellEffect[] effects;

    /**
     * Default constructor just initializes the spell stage's components to their default value
     */
    public SpellStage()
    {
        // Null spell delivery method is default
        this.deliveryMethod = null;
        // Spell stages can have 4 effects
        this.effects = new SpellEffect[MAX_EFFECTS_PER_STAGE];
    }

    /**
     * Constructor that takes in an NBT compound and creates the spell stage from NBT
     *
     * @param spellStageNBT The NBT containing the spell stage's information
     */
    SpellStage(NBTTagCompound spellStageNBT)
    {
        this.deserializeNBT(spellStageNBT);
    }

    /**
     * Gets the cost of the spell stage if it were fired
     *
     * @return The cost of this specific spell stage
     */
    public double getCost()
    {
        // Ensure the stage is valid first, otherwise cost is 0
        if (this.isValid())
        {
            // Grab the cost of the delivery method
            double cost = this.deliveryMethod.getCost();
            // Go over every effect and add its cost
            for (SpellEffect effect : this.effects)
            {
                if (effect != null)
                {
                    // Multiply each effect's cost by the delivery method multiplier
                    cost = cost + this.deliveryMethod.getStageCostMultiplier() * effect.getCost();
                }
            }
            return cost;
        }
        return 0;
    }

    /**
     * Returns true if this spell stage is ready to fire, false otherwise
     *
     * @return True if the delivery method is non-null
     */
    public boolean isValid()
    {
        return this.deliveryMethod != null;
    }

    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell stage's data
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        // The spell stage delivery method can be null, double check that it isn't before writing it and its state
        if (this.deliveryMethod != null)
        {
            nbt.setTag(NBT_DELIVERY_METHOD, this.deliveryMethod.serializeNBT());
        }
        // The spell stage effects can be null, so we need to skip null effects
        for (int i = 0; i < this.effects.length; i++)
        {
            // Grab the spell effect to write
            SpellEffect spellEffect = this.effects[i];
            // If the spell effect is not null write it
            if (spellEffect != null)
            {
                nbt.setTag(NBT_EFFECT_BASE + i, spellEffect.serializeNBT());
            }
        }

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
        // The spell stage delivery method can be null, double check that it exists before reading it and its state
        if (nbt.hasKey(NBT_DELIVERY_METHOD))
        {
            this.deliveryMethod = SpellDeliveryMethod.createFromNBT(nbt.getCompoundTag(NBT_DELIVERY_METHOD));
        }
        // Initialize the array
        this.effects = new SpellEffect[MAX_EFFECTS_PER_STAGE];
        // Go over each spell effect
        for (int i = 0; i < this.effects.length; i++)
        {
            // The spell stage effects can be null, so we need to skip null effects
            if (nbt.hasKey(NBT_EFFECT_BASE + i))
            {
                this.effects[i] = SpellEffect.createFromNBT(nbt.getCompoundTag(NBT_EFFECT_BASE + i));
            }
        }
    }

    ///
    /// Getters/Setters
    ///

    public void setDeliveryMethod(SpellDeliveryMethod deliveryMethod)
    {
        this.deliveryMethod = deliveryMethod;
    }

    public SpellDeliveryMethod getDeliveryMethod()
    {
        return deliveryMethod;
    }

    public SpellEffect[] getEffects()
    {
        return effects;
    }
}
