package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Effect that sets fire to the hit target
 */
public class SpellEffectBurn extends AOTDSpellEffect
{
    // NBT constants for burn duration
    private static final String NBT_BURN_DURATION = "burn_duration";

    // The default burn duration
    private static final int DEFAULT_BURN_DURATION = 2;

    // The burn duration this effect gives
    private int burnDuration = DEFAULT_BURN_DURATION;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectBurn()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Burn Duration",
                "The number of seconds to set fire to when hitting entities.",
                () -> Integer.toString(this.burnDuration),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the burn duration
                        this.burnDuration = Integer.parseInt(newValue);
                        // Ensure burn duration is valid
                        if (this.burnDuration > 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.burnDuration = DEFAULT_BURN_DURATION;
                            return "Burn duration must be larger than 0";
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
        return 10 + burnDuration * 5;
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
        this.createParticlesAt(3, 5, new Vec3d(entityHit.posX, entityHit.posY, entityHit.posZ), entityHit.dimension);
        entityHit.setFire(this.burnDuration);
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
        if (world.getBlockState(position.up()).getBlock() instanceof BlockAir)
        {
            if (!(world.getBlockState(position).getBlock() instanceof BlockAir))
            {
                this.createParticlesAt(1, 3, new Vec3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5), world.provider.getDimension());
                world.setBlockState(position.up(), Blocks.FIRE.getDefaultState());
            }
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
        return ModSpellEffects.BURN;
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

        nbt.setInteger(NBT_BURN_DURATION, this.burnDuration);

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
        this.burnDuration = nbt.getInteger(NBT_BURN_DURATION);
    }
}
