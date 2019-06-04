package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Projectile delivery method delivers the spell to the target with a projectile
 */
public class SpellDeliveryMethodProjectile extends AOTDSpellDeliveryMethod
{
    // The NBT keys
    private static final String NBT_SPEED = "speed";

    // The speed of the projectile
    private double speed = 1.0;

    /**
     * Constructor initializes the editable properties
     */
    public SpellDeliveryMethodProjectile()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty("Speed", "The speed of the projectile", () -> Double.toString(this.speed), newValue ->
        {
            // Ensure the number is parsable
            if (NumberUtils.isParsable(newValue))
            {
                // Parse the speed
                this.speed = Double.parseDouble(newValue);
                // Ensure speed is valid
                if (this.speed >= 0.25)
                {
                    return null;
                }
                else
                {
                    return "Speed must be at least 0.25";
                }
            }
            // If it's not valid return an error
            else
            {
                return newValue + " is not a valid decimal number!";
            }
        }));
    }

    /**
     * @return Gets the projectile speed
     */
    public double getSpeed()
    {
        return speed;
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
        return 5 + this.speed;
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

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setDouble(NBT_SPEED, this.speed);

        return nbt;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        this.speed = nbt.getDouble(NBT_SPEED);
    }
}
