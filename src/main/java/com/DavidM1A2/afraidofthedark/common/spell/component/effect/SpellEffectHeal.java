package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Effect that heals a hit entity
 */
public class SpellEffectHeal extends AOTDSpellEffect
{
    // NBT constants for healing amount
    private static final String NBT_HEALING_AMOUNT = "healing_amount";

    // The amount of healing this effect gives
    private int healAmount = 2;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectHeal()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Heal Amount")
                .withDescription("The amount of half hearts to restore.")
                .withSetter(newValue -> this.healAmount = newValue)
                .withGetter(() -> this.healAmount)
                .withDefaultValue(2)
                .withMinValue(1)
                .build());
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
            createParticlesAt(1, 3, state.getPosition(), entity.dimension);
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
