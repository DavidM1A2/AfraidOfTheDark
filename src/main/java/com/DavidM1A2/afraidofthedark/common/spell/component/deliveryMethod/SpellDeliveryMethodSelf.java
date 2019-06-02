package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Self delivery method delivers the spell to the target with a self entity
 */
public class SpellDeliveryMethodSelf extends AOTDSpellDeliveryMethod
{
    /**
     * Constructor does not initialize anything
     */
    public SpellDeliveryMethodSelf()
    {
        super();
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param spell The spell that is being delivered
     * @param spellIndex The current spell stage index
     * @param source The entity that was the source of the spell
     */
    @Override
    public void deliver(Spell spell, int spellIndex, Entity source)
    {
        // Delivery is as simple as applying effects for the "self" delivery method
        spell.getStage(spellIndex).forAllValidEffects(spellEffect ->
        {
            ISpellDeliveryEffectApplicator effectApplicator = this.getEntryRegistryType().getApplicator(spellEffect.getEntryRegistryType());
            effectApplicator.applyEffect(spellEffect, source);
        });

        // Grab the next delivery method
        SpellDeliveryMethod nextDeliveryMethod = spell.hasStage(spellIndex + 1) ? spell.getStage(spellIndex + 1).getDeliveryMethod() : null;
        if (nextDeliveryMethod != null)
        {
            // Perform the transition between the next delivery method and the current delivery method
            ISpellDeliveryTransitioner spellDeliveryTransitioner = nextDeliveryMethod.getEntryRegistryType().getTransitioner(this.getEntryRegistryType());
            spellDeliveryTransitioner.transitionThroughEntity(spell, spellIndex, source);
        }
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param spell The spell that is being delivered
     * @param spellIndex The current spell stage index
     * @param world The world the spell was cast in
     * @param position The position the delivery was cast at
     * @param direction The direction the delivery should happen at
     */
    @Override
    public void deliver(Spell spell, int spellIndex, World world, Vec3d position, Vec3d direction)
    {
        // Don't transition or apply effects, self can't be applied to a block
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 0;
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    @Override
    public double getStageCostMultiplier()
    {
        return 1.0;
    }

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public SpellDeliveryMethodEntry getEntryRegistryType()
    {
        return ModSpellDeliveryMethods.SELF;
    }
}
