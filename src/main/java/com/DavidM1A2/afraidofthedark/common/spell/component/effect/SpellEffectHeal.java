package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

/**
 * Effect that heals a hit entity
 */
public class SpellEffectHeal extends AOTDSpellEffect
{
    // NBT constants for healing amount
    private static final String NBT_HEALING_AMOUNT = "healing_amount";

    // The default heal amount
    private static final int DEFAULT_HEAL_AMOUNT = 2;

    // The amount of healing this effect gives
    private int healAmount = DEFAULT_HEAL_AMOUNT;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectHeal()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Heal Amount",
                "The amount of healing to do in half-hearts.",
                () -> Integer.toString(this.healAmount),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the heal amount
                        this.healAmount = Integer.parseInt(newValue);
                        // Ensure heal amount is valid
                        if (this.healAmount > 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.healAmount = DEFAULT_HEAL_AMOUNT;
                            return "Heal amount must be larger than 0";
                        }
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        return newValue + " is not a valid integer!";
                    }
                }
        ));
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return healAmount * 2;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        Entity entity = state.getEntity();
        if (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))
        {
            this.createParticlesAt(1, 3, new Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension);
            ((EntityLivingBase) entity).heal(this.healAmount);
        }
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.HEAL;
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

        nbt.setInteger(NBT_HEALING_AMOUNT, this.healAmount);

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
        this.healAmount = nbt.getInteger(NBT_HEALING_AMOUNT);
    }
}
