package com.DavidM1A2.afraidofthedark.common.spell.component.effect.base;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
     * Performs the effect against a given entity
     *
     * @param spell The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex The effect slot that this effect is in
     * @param entityHit The entity that the effect should be applied to
     */
    public abstract void performEffect(Spell spell, int spellStageIndex, int effectIndex, Entity entityHit);

    /**
     * Performs the effect at a given position in the world
     *
     * @param spell The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex The effect slot that this effect is in
     * @param world The world the effect is being fired in
     * @param position The position the effect is being performed at
     */
    public abstract void performEffect(Spell spell, int spellStageIndex, int effectIndex, World world, BlockPos position);

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
