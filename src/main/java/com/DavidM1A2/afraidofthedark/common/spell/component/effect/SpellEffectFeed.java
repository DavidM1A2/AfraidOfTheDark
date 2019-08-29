package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

/**
 * Effect that feeds a hit player
 */
public class SpellEffectFeed extends AOTDSpellEffect
{
    // NBT constants
    private static final String NBT_HUNGER_VALUE = "hunger_value";
    private static final String NBT_SATURATION_VALUE = "saturation_value";

    // Default values
    private static final int DEFAULT_HUNGER_VALUE = 2;
    private static final int DEFAULT_SATURATION_VALUE = 1;

    // The amount of hunger bars this effect gives
    private int hungerValue = DEFAULT_HUNGER_VALUE;
    // The amount of saturation this effect gives
    private int saturationValue = DEFAULT_SATURATION_VALUE;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectFeed()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Hunger Value",
                "The amount of food half 'drumsticks' to restore.",
                () -> Integer.toString(this.hungerValue),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the hunger
                        this.hungerValue = Integer.parseInt(newValue);
                        // Ensure hunger value is valid
                        if (this.hungerValue > 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.hungerValue = DEFAULT_HUNGER_VALUE;
                            return "Hunger value must be larger than 0";
                        }
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        return newValue + " is not a valid integer!";
                    }
                }
        ));
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Saturation Value",
                "The amount of saturation restore.",
                () -> Integer.toString(this.saturationValue),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the saturation
                        this.saturationValue = Integer.parseInt(newValue);
                        // Ensure saturation value is valid
                        if (this.saturationValue >= 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.saturationValue = DEFAULT_SATURATION_VALUE;
                            return "Saturation value must be positive";
                        }
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        return newValue + " is not a valid integer!";
                    }
                }
        ));
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return this.hungerValue / 2.0 + this.saturationValue * 2.0;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        if (state.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) state.getEntity();
            createParticlesAt(1, 2, state.getPosition(), entityPlayer.dimension);
            FoodStats foodStats = entityPlayer.getFoodStats();
            foodStats.addStats(this.hungerValue, this.saturationValue);
        }
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.FEED;
    }

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setInteger(NBT_HUNGER_VALUE, this.hungerValue);
        nbt.setInteger(NBT_SATURATION_VALUE, this.saturationValue);

        return nbt;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        this.hungerValue = nbt.getInteger(NBT_HUNGER_VALUE);
        this.saturationValue = nbt.getInteger(NBT_SATURATION_VALUE);
    }
}
