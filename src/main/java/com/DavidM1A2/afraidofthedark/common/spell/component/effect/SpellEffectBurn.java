package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.DeliveryTransitionState;
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
            this.createParticlesAt(3, 5, new Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension);
            entity.setFire(this.burnDuration);
        }
        else
        {
            World world = state.getWorld();
            BlockPos position = new BlockPos(state.getPosition());
            if (world.getBlockState(position.up()).getBlock() instanceof BlockAir)
            {
                if (!(world.getBlockState(position).getBlock() instanceof BlockAir))
                {
                    this.createParticlesAt(1, 3, new Vec3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5), world.provider.getDimension());
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
