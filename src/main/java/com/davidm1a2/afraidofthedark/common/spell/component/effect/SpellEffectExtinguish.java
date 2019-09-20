package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Effect that extinguishes fire
 */
public class SpellEffectExtinguish extends AOTDSpellEffect
{
    /**
     * Constructor just calls super
     */
    public SpellEffectExtinguish()
    {
        super();
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 15;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        // If we hit an entity extinguish them
        if (state.getEntity() != null)
        {
            Entity entity = state.getEntity();
            createParticlesAt(3, 5, new Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension);
            entity.extinguish();
        }
        // If we hit a fire block clear the fire
        else
        {
            World world = state.getWorld();
            BlockPos position = state.getBlockPosition();
            if (world.getBlockState(position).getBlock() instanceof BlockFire)
            {
                createParticlesAt(1, 3, state.getPosition(), world.provider.getDimension());
                world.setBlockState(position, Blocks.AIR.getDefaultState());
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
        return ModSpellEffects.EXTINGUISH;
    }
}
