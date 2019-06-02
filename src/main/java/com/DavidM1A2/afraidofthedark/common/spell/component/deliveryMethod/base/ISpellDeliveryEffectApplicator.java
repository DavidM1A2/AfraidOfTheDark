package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Interface representing how effects are applied based on the delivery method used
 */
public interface ISpellDeliveryEffectApplicator
{
    /**
     * Performs the effect against a given entity
     *
     * @param effect    The effect that needs to be applied
     * @param entityHit The entity that the effect should be applied to
     */
    void applyEffect(SpellEffect effect, Entity entityHit);

    /**
     * Performs the effect at a given position in the world
     *
     * @param effect   The effect that needs to be applied
     * @param world    The world the effect is being fired in
     * @param position The position the effect is being performed at
     */
    void applyEffect(SpellEffect effect, World world, BlockPos position);
}
