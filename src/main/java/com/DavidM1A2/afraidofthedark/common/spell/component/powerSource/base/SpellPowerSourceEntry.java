package com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base;

import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponentEntry;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

/**
 * Entry used to store a reference to a power source
 */
public class SpellPowerSourceEntry extends SpellComponentEntry<SpellPowerSourceEntry, SpellPowerSource>
{
    /**
     * Constructor just passes on the id and factory
     *
     * @param id The ID of this power source entry
     * @param factory The factory that makes new instances of this power source
     */
    public SpellPowerSourceEntry(ResourceLocation id, Supplier<SpellPowerSource> factory)
    {
        super(id, factory);
    }
}
