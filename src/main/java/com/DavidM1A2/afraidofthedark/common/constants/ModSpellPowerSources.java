package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.SpellPowerSourceCreative;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource;

/**
 * A static class containing all of our spell power source references for us
 */
public class ModSpellPowerSources
{
    public static final SpellPowerSource CREATIVE = new SpellPowerSourceCreative();

    // An array containing a list of spell power sources that AOTD adds
    public static final SpellPowerSource[] SPELL_POWER_SOURCES = new SpellPowerSource[]
            {
                    CREATIVE
            };
}
