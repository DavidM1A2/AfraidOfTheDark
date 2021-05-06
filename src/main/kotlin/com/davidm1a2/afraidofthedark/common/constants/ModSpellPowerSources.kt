package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.CreativeSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.ExperienceSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.FlaskSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.HealthSpellPowerSource

/**
 * A static class containing all of our spell power source references for us
 */
object ModSpellPowerSources {
    val CREATIVE = CreativeSpellPowerSource()
    val EXPERIENCE = ExperienceSpellPowerSource()
    val FLASK = FlaskSpellPowerSource()
    val HEALTH = HealthSpellPowerSource()

    // An array containing a list of spell power sources that AOTD adds
    val SPELL_POWER_SOURCES = arrayOf(
        CREATIVE,
        EXPERIENCE,
        FLASK,
        HEALTH
    )
}