package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Effect that creates an explosion at the given position
 */
public class SpellEffectExplosion extends AOTDSpellEffect
{
    // NBT constants
    private static final String NBT_RADIUS = "radius";

    // The radius of the explosion
    private double radius = 5;

    /**
     * Constructor initializes the editable properties
     */
    public SpellEffectExplosion()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.doubleProperty()
                .withName("Radius")
                .withDescription("The explosion's radius.")
                .withSetter(newValue -> this.radius = newValue)
                .withGetter(() -> this.radius)
                .withDefaultValue(5D)
                .withMinValue(1D)
                .withMaxValue(150D)
                .build());
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
