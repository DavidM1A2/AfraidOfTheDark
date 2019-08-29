package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Effect that creates an explosion at the given position
 */
public class SpellEffectExplosion extends AOTDSpellEffect
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
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        World world = state.getWorld();
        Vec3d position = state.getPosition();
        createParticlesAt(1, 3, position, world.provider.getDimension());
        world.createExplosion(null, position.x, position.y, position.z, (float) this.radius, true);
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
