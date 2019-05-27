package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;

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
     * Should get the SpellComponentEntry registry's name
     *
     * @return The name of the registry entry that this component was built with, used for deserialization
     */
    @Override
    public String getEntryRegistryName()
    {
        return ModSpellDeliveryMethods.SELF.getRegistryName().toString();
    }
}
