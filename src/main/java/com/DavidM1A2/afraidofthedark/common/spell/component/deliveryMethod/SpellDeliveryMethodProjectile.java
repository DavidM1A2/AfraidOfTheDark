package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Projectile delivery method delivers the spell to the target with a projectile
 */
public class SpellDeliveryMethodProjectile extends AOTDSpellDeliveryMethod
{
    /**
     * Constructor does not initialize anything
     */
    public SpellDeliveryMethodProjectile()
    {
        super();
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param spell      The spell that is being delivered
     * @param spellIndex The current spell stage index
     * @param source     The entity that was the source of the spell
     */
    @Override
    public void deliver(Spell spell, int spellIndex, Entity source)
    {
        EntitySpellProjectile spellProjectile = new EntitySpellProjectile(source.world, spell, spellIndex, source);
        source.world.spawnEntity(spellProjectile);
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param spell      The spell that is being delivered
     * @param spellIndex The current spell stage index
     * @param world      The world the spell was cast in
     * @param position   The position the delivery was cast at
     * @param direction  The direction the delivery should happen at
     */
    @Override
    public void deliver(Spell spell, int spellIndex, World world, Vec3d position, Vec3d direction)
    {
        EntitySpellProjectile spellProjectile = new EntitySpellProjectile(world, spell, spellIndex, position.x, position.y, position.z, direction.x, direction.y, direction.z);
        world.spawnEntity(spellProjectile);
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 5;
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    @Override
    public double getStageCostMultiplier()
    {
        return 1.1;
    }

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public SpellDeliveryMethodEntry getEntryRegistryType()
    {
        return ModSpellDeliveryMethods.PROJECTILE;
    }
}
