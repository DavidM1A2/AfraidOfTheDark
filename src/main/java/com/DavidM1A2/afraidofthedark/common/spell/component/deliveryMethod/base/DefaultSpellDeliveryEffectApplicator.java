package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class representing how effects are applied based on the delivery method used
 */
public class DefaultSpellDeliveryEffectApplicator implements ISpellDeliveryEffectApplicator
{
    /**
     * Performs the effect against a given entity
     *
     * @param spell           The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex     The effect slot that this effect is in
     * @param entityHit The entity that the effect should be applied to
     */
    @Override
    public void applyEffect(Spell spell, int spellStageIndex, int effectIndex, Entity entityHit)
    {
        SpellEffect effect = spell.getStage(spellStageIndex).getEffects()[effectIndex];
        effect.performEffect(spell, spellStageIndex, effectIndex, entityHit);
    }

    /**
     * Performs the effect at a given position in the world
     *
     * @param spell           The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex     The effect slot that this effect is in
     * @param world    The world the effect is being fired in
     * @param position The position the effect is being performed at
     */
    @Override
    public void applyEffect(Spell spell, int spellStageIndex, int effectIndex, World world, BlockPos position)
    {
        SpellEffect effect = spell.getStage(spellStageIndex).getEffects()[effectIndex];
        effect.performEffect(spell, spellStageIndex, effectIndex, world, position);
    }
}
