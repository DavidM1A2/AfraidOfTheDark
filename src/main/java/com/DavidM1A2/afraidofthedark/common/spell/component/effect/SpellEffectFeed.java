package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Effect that feeds a hit player
 */
public class SpellEffectFeed extends SpellEffect
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
     * Performs the effect against a given entity
     *
     * @param spell           The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex     The effect slot that this effect is in
     * @param entityHit       The entity that the effect should be applied to
     */
    @Override
    public void performEffect(Spell spell, int spellStageIndex, int effectIndex, Entity entityHit)
    {
        if (entityHit instanceof EntityPlayer)
        {
            FoodStats foodStats = ((EntityPlayer) entityHit).getFoodStats();
            foodStats.addStats(this.hungerValue, this.saturationValue);
        }
    }

    /**
     * Performs the effect at a given position in the world
     *
     * @param spell           The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex     The effect slot that this effect is in
     * @param world           The world the effect is being fired in
     * @param position        The position the effect is being performed at
     */
    @Override
    public void performEffect(Spell spell, int spellStageIndex, int effectIndex, World world, BlockPos position)
    {
        // No Effect on blocks
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
