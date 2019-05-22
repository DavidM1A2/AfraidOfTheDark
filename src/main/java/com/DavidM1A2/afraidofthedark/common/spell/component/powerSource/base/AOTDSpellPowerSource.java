package com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all AOTD spell power sources
 */
public abstract class AOTDSpellPowerSource extends SpellPowerSource
{
    /**
     * Constructor sets the power source's name
     *
     * @param name The name of the power source
     */
    public AOTDSpellPowerSource(String name)
    {
        super();
        this.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
    }
}
