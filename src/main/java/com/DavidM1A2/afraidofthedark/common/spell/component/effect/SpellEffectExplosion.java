package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Effect that creates an explosion at the given position
 */
public class SpellEffectExplosion extends SpellEffect
{
    // NBT constants
    private static final String NBT_RADIUS = "radius";

    private static final double DEFAULT_RADIUS = 5;

    // The radius of the explosion
    private double radius = DEFAULT_RADIUS;

    /**
     * Constructor initializes the editable properties
     */
    public SpellEffectExplosion()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty("Radius", "The explosion radius", () -> Double.toString(this.radius), newValue ->
        {
            // Ensure the number is parsable
            if (NumberUtils.isParsable(newValue))
            {
                // Parse the radius
                this.radius = Double.parseDouble(newValue);
                // Ensure radius is valid
                if (this.radius > 0)
                {
                    return null;
                }
                else
                {
                    this.radius = DEFAULT_RADIUS;
                    return "Radius must be larger than 0";
                }
            }
            // If it's not valid return an error
            else
            {
                return newValue + " is not a valid decimal number!";
            }
        }));
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

        nbt.setDouble(NBT_RADIUS, this.radius);

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
        this.radius = nbt.getDouble(NBT_RADIUS);
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 10 + radius * radius;
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
        entityHit.world.createExplosion(entityHit, entityHit.posX, entityHit.posY, entityHit.posZ, (float) this.radius, true);
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
        world.createExplosion(null, position.getX(), position.getY(), position.getZ(), (float) this.radius, true);
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.EXPLOSION;
    }
}
