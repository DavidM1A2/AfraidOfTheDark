package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
        BlockPos position = new BlockPos(state.getPosition());
        IBlockState blockState = world.getBlockState(position);
        // If we hit a farmland block check the crops above and see if they need growing
        if (blockState.getBlock() instanceof BlockFarmland)
        {
            blockState = world.getBlockState(position.up());
        }
        // Grob the block at the current position if it's a type 'IGrowable'
        if (blockState.getBlock() instanceof IGrowable)
        {
            this.createParticlesAt(1, 3, new Vec3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5), world.provider.getDimension());
            ((IGrowable) blockState.getBlock()).grow(world, world.rand, position.up(), blockState);
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
