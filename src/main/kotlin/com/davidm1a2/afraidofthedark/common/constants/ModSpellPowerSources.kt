package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.AlchemySpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.CreativeSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.CrystalSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.ExperienceSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.HealthSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.LunarSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.SolarSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.VitaeLanternSpellPowerSource

/**
 * A static class containing all of our spell power source references for us
 */
object ModSpellPowerSources {
    val CREATIVE = CreativeSpellPowerSource()
    val EXPERIENCE = ExperienceSpellPowerSource()
    val HEALTH = HealthSpellPowerSource()
    val ALCHEMY = AlchemySpellPowerSource()
    val VITAE_LANTERN = VitaeLanternSpellPowerSource()
    val CRYSTAL = CrystalSpellPowerSource()
    val LUNAR = LunarSpellPowerSource()
    val SOLAR = SolarSpellPowerSource()

    // An array containing a list of spell power sources that AOTD adds
    val SPELL_POWER_SOURCES = arrayOf(
        CREATIVE,
        EXPERIENCE,
        HEALTH,
        ALCHEMY,
        VITAE_LANTERN,
        CRYSTAL,
        LUNAR,
        SOLAR
    )
}