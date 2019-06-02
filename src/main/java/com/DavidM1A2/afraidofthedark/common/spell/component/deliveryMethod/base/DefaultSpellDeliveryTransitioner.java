package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Class representing a delivery method transitioner to go from one delivery method to another
 */
public class DefaultSpellDeliveryTransitioner implements ISpellDeliveryTransitioner
{
    /**
     * Executes the delivery method from an entity
     *
     * @param spell      The spell reference
     * @param stageIndex The current spell stage index
     * @param entity     The entity the delivery method is being executed from
     */
    @Override
    public void transitionThroughEntity(Spell spell, int stageIndex, Entity entity)
    {
        // Advance to the the next delivery method
        stageIndex++;

        // If the spell has this stage execute it
        if (spell.hasStage(stageIndex))
        {
            spell.getStage(stageIndex)
                    .getDeliveryMethod()
                    .deliver(spell, stageIndex, entity);
        }
        // Spell complete
    }

    /**
     * Executes the delivery method from a position and a direction which can be null
     *
     * @param spell      The spell reference
     * @param stageIndex The current spell stage index
     * @param world      The world the spell is being executed in
     * @param position   The position the delivery method should execute at
     * @param direction  The direction the delivery method should be executed in, can be null
     */
    @Override
    public void transitionAtPosition(Spell spell, int stageIndex, World world, Vec3d position, Vec3d direction)
    {
        // Advance to the the next delivery method
        stageIndex++;

        // If the spell has this stage execute it
        if (spell.hasStage(stageIndex))
        {
            spell.getStage(stageIndex)
                    .getDeliveryMethod()
                    .deliver(spell, stageIndex, world, position, direction);
        }
        // Spell complete
    }
}
