package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Dig effect digs a block
 */
public class SpellEffectDig extends AOTDSpellEffect
{
    /**
     * Constructor just calls super, there's nothing else to init
     */
    public SpellEffectDig()
    {
        super();
    }

    /**
     * Gets the cost of the effect given the state of the effect
     *
     * @return The cost of dig is 14
     */
    @Override
    public double getCost()
    {
        return 14;
    }

    /**
     * Performs the dig effect against a given entity which removes the block under them
     *
     * @param spell The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex The effect slot that this effect is in
     * @param entityHit The entity that the effect should be applied to
     */
    @Override
    public void performEffect(Spell spell, int spellStageIndex, int effectIndex, Entity entityHit)
    {
        // Digs the block under the player
        BlockPos blockPos = entityHit.getPosition().down();
        if (this.canBlockBeDestroyed(entityHit.world, blockPos))
        {
            entityHit.world.destroyBlock(blockPos, true);
        }
    }

    /**
     * Performs the effect against a given block in the world
     *
     * @param spell The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex The effect slot that this effect is in
     * @param world The world the effect is being fired in
     * @param position The position the effect is being performed at
     */
    @Override
    public void performEffect(Spell spell, int spellStageIndex, int effectIndex, World world, BlockPos position)
    {
        // Digs the block at the position
        if (this.canBlockBeDestroyed(world, position))
        {
            world.destroyBlock(position, true);
        }
    }

    /**
     * Tests if a given block can be broken with a dig spell
     *
     * @param world The world the block is in
     * @param blockPos The pos the block is at
     * @return True if the block can be destroyed, false otherwise
     */
    private boolean canBlockBeDestroyed(World world, BlockPos blockPos)
    {
        IBlockState blockState = world.getBlockState(blockPos);
        return blockState.getBlock().getBlockHardness(blockState, world, blockPos) != -1;
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.DIG;
    }
}
