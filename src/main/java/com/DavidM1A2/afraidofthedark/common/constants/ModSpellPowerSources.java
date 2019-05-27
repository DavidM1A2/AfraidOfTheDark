package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.SpellPowerSourceCreative;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our spell power source references for us
 */
public class ModSpellPowerSources
{
    public static final SpellPowerSourceEntry CREATIVE = new SpellPowerSourceEntry(new ResourceLocation(Constants.MOD_ID, "creative"), SpellPowerSourceCreative::new);

    // An array containing a list of spell power sources that AOTD adds
    public static final SpellPowerSourceEntry[] SPELL_POWER_SOURCES = new SpellPowerSourceEntry[]
            {
                    CREATIVE
            };
}
