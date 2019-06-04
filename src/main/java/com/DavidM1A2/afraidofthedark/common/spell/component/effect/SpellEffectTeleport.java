package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Teleports the spell owner to the hit location
 */
public class SpellEffectTeleport extends SpellEffect
{
    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 3;
    }

    /**
     * Performs the effect against a given entity
     *
     * @param entityHit The entity that the effect should be applied to
     */
    @Override
    public void performEffect(Entity entityHit)
    {

    }

    /**
     * Performs the effect at a given position in the world
     *
     * @param world    The world the effect is being fired in
     * @param position The position the effect is being performed at
     */
    @Override
    public void performEffect(World world, BlockPos position)
    {

    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.TELEPORT;
    }
}
