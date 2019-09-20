package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Spell effect that causes growable blocks to grow
 */
public class SpellEffectGrow extends AOTDSpellEffect
{
    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 7;
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
        BlockPos position = state.getBlockPosition();
        IBlockState blockState = world.getBlockState(position);
        // If we hit a block that crops might be on check the block above and see if we can grow on that instead
        if (!(blockState.getBlock() instanceof IGrowable))
        {
            position = position.up();
            blockState = world.getBlockState(position);
        }
        // Grob the block at the current position if it's a type 'IGrowable'
        if (blockState.getBlock() instanceof IGrowable)
        {
            createParticlesAt(1, 3, state.getPosition(), world.provider.getDimension());
            ((IGrowable) blockState.getBlock()).grow(world, world.rand, position, blockState);
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
        return ModSpellEffects.GROW;
    }
}
