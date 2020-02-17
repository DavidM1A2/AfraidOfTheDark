package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.SpellPowerSourceCreative
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.SpellPowerSourceExperience

/**
 * A static class containing all of our spell power source references for us
 */
object ModSpellPowerSources
{
    val CREATIVE = SpellPowerSourceCreative()
    val EXPERIENCE = SpellPowerSourceExperience()

    // An array containing a list of spell power sources that AOTD adds
    val SPELL_POWER_SOURCES = arrayOf(
            CREATIVE,
            EXPERIENCE
    )
}