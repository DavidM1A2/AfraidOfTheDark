package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
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

    // The burn duration this effect gives
    private int burnDuration = 2;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectBurn()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Burn")
                .withDescription("The number of seconds to set fire to when hitting entities.")
                .withSetter(newValue -> this.burnDuration = newValue)
                .withGetter(() -> this.burnDuration)
                .withDefaultValue(2)
                .withMinValue(1)
                .withMaxValue(60)
                .build());
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
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        if (state.getEntity() != null)
        {
            Entity entity = state.getEntity();
            createParticlesAt(3, 5, new Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension);
            entity.setFire(this.burnDuration);
        }
        else
        {
            World world = state.getWorld();
            BlockPos position = state.getBlockPosition();
            if (world.getBlockState(position.up()).getBlock() instanceof BlockAir)
            {
                if (!(world.getBlockState(position).getBlock() instanceof BlockAir))
                {
                    createParticlesAt(1, 3, state.getPosition(), world.provider.getDimension());
                    world.setBlockState(position.up(), Blocks.FIRE.getDefaultState());
                }
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
