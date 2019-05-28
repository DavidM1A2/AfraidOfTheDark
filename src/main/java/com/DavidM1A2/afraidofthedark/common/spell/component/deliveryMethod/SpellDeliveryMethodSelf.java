package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.SpellStage;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
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
     * Ignored, can't cast self on a position
     *
     * @param spell The spell reference
     * @param stageIndex The current spell stage index
     * @param world The world the delivery method is being executed in
     * @param position The position the delivery method should execute at
     * @param direction The direction the delivery method should be executed in, can be null
     */
    @Override
    public void executeDeliveryFrom(Spell spell, int stageIndex, World world, Vec3d position, Vec3d direction)
    {
        // Perform no effects

        // Execute the next delivery method
        stageIndex++;

        if (spell.hasStage(stageIndex))
        {
            spell.getStage(stageIndex).getDeliveryMethod().executeDeliveryFrom(spell, stageIndex, world, position, direction);
        }
        else
        {
            // Spell complete
        }
    }

    /**
     * Casts the effects on a given entity
     *
     * @param spell The spell reference
     * @param stageIndex The current spell stage index
     * @param entity The entity the delivery method is being executed from
     */
    @Override
    public void executeDeliveryFrom(Spell spell, int stageIndex, Entity entity)
    {
        // Perform each effect against the entity
        SpellStage spellStage = spell.getStage(stageIndex);
        for (SpellEffect effect : spellStage.getEffects())
        {
            if (effect != null)
            {
                effect.performEffect(entity);
            }
        }

        // Execute the next delivery method
        stageIndex++;

        if (spell.hasStage(stageIndex))
        {
            spell.getStage(stageIndex).getDeliveryMethod().executeDeliveryFrom(spell, stageIndex, entity);
        }
        else
        {
            // Spell complete
        }
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
