package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
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
     * @param entityHit The entity that the effect should be applied to
     */
    @Override
    public void performEffect(Entity entityHit)
    {
        // Digs the block under the player
        BlockPos blockPos = entityHit.getPosition().down();
        entityHit.world.destroyBlock(blockPos, true);
    }

    /**
     * Performs the effect against a given block in the world
     *
     * @param world The world the effect is being fired in
     * @param position The position the effect is being performed at
     */
    @Override
    public void performEffect(World world, BlockPos position)
    {
        // Digs the block at the position
        world.destroyBlock(position, true);
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
