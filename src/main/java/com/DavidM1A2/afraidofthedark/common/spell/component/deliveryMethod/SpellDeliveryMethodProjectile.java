package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Projectile delivery method delivers the spell to the target with a projectile
 */
public class SpellDeliveryMethodProjectile extends AOTDSpellDeliveryMethod
{
    // The NBT keys
    private static final String NBT_SPEED = "speed";
    private static final String NBT_RANGE = "range";

    // The default speed value
    private static final double DEFAULT_SPEED = 0.6;
    // The default range value
    private static final double DEFAULT_RANGE = 50;

    // The speed of the projectile
    private double speed = DEFAULT_SPEED;
    // The range of the projectile
    private double range = DEFAULT_RANGE;

    /**
     * Constructor initializes the editable properties
     */
    public SpellDeliveryMethodProjectile()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty("Range", "The range of the projectile in blocks", () -> Double.toString(this.range), newValue ->
        {
            // Ensure the number is parsable
            if (NumberUtils.isParsable(newValue))
            {
                // Parse the range
                this.range = Double.parseDouble(newValue);
                // Ensure range is valid
                if (this.range >= 1)
                {
                    return null;
                }
                else
                {
                    this.range = DEFAULT_RANGE;
                    return "Range must be at least 1";
                }
            }
            // If it's not valid return an error
            else
            {
                return newValue + " is not a valid decimal number!";
            }
        }));
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
                    this.speed = DEFAULT_SPEED;
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
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void executeDelivery(DeliveryTransitionState state)
    {
        EntitySpellProjectile spellProjectile;
        if (state.getEntity() != null)
        {
            spellProjectile = new EntitySpellProjectile(state.getWorld(), state.getSpell(), state.getStageIndex(), state.getEntity());
        }
        else
        {
            spellProjectile = new EntitySpellProjectile(state.getWorld(), state.getSpell(), state.getStageIndex(), state.getPosition(), state.getDirection());
        }
        state.getWorld().spawnEntity(spellProjectile);
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    @Override
    public void defaultEffectProc(DeliveryTransitionState state, SpellEffect effect)
    {
        effect.procEffect(state);
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    @Override
    public void performDefaultTransition(DeliveryTransitionState state)
    {
        Spell spell = state.getSpell();
        int spellIndex = state.getStageIndex();
        // Perform the transition between the next delivery method and the current delivery method
        spell.getStage(spellIndex + 1).getDeliveryMethod().executeDelivery(new DeliveryTransitionStateBuilder()
                .copyOf(state)
                .withStageIndex(spellIndex + 1)
                .withDeliveryEntity(null)
                .build());
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 5 + this.speed + this.range * this.range / 15.0;
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
        nbt.setDouble(NBT_RANGE, this.range);

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
        this.range = nbt.getDouble(NBT_RANGE);
    }

    /**
     * @return Gets the projectile speed
     */
    public double getSpeed()
    {
        return speed;
    }

    /**
     * @return Gets the projectile range
     */
    public double getRange()
    {
        return range;
    }
}
