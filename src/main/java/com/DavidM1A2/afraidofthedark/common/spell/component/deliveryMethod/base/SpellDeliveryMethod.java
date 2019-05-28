package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Base class for all spell delivery methods
 */
public abstract class SpellDeliveryMethod extends SpellComponent
{
    /**
     * Executes the delivery method from a position and a direction which can be null
     *
     * @param spell The spell reference
     * @param stageIndex The current spell stage index
     * @param world The world the spell is being executed in
     * @param position The position the delivery method should execute at
     * @param direction The direction the delivery method should be executed in, can be null
     */
    public abstract void executeDeliveryFrom(Spell spell, int stageIndex, World world, Vec3d position, Vec3d direction);

    /**
     * Executes the delivery method from an entity
     *
     * @param spell The spell reference
     * @param stageIndex The current spell stage index
     * @param entity The entity the delivery method is being executed from
     */
    public abstract void executeDeliveryFrom(Spell spell, int stageIndex, Entity entity);

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    public abstract double getCost();

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    public abstract double getStageCostMultiplier();

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public abstract SpellDeliveryMethodEntry getEntryRegistryType();

    /**
     * Utility function to create a spell delivery method from NBT
     *
     * @param nbt The NBT to get the delivery method information from
     * @return The spell delivery method instance from NBT
     */
    public static SpellDeliveryMethod createFromNBT(NBTTagCompound nbt)
    {
        // Figure out the type of delivery method that this NBT represents
        String deliveryMethodTypeId = nbt.getString(NBT_TYPE_ID);
        // Use our registry to create a new instance of this type
        SpellDeliveryMethod deliveryMethod = ModRegistries.SPELL_DELIVERY_METHODS.getValue(new ResourceLocation(deliveryMethodTypeId)).newInstance();
        // Load in the delivery method's state from NBT
        deliveryMethod.deserializeNBT(nbt);
        // Return the delivery method
        return deliveryMethod;
    }
}
