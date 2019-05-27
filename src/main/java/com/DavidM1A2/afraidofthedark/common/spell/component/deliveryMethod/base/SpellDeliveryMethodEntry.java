package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponentEntry;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

/**
 * Entry used to store a reference to a delivery method
 */
public class SpellDeliveryMethodEntry extends SpellComponentEntry<SpellDeliveryMethodEntry, SpellDeliveryMethod>
{
    /**
     * Constructor just passes on the id and factory
     *
     * @param id The ID of this delivery method entry
     * @param factory The factory that makes new instances of this delivery method
     */
    public SpellDeliveryMethodEntry(ResourceLocation id, Supplier<SpellDeliveryMethod> factory)
    {
        super(id, factory);
    }
}
