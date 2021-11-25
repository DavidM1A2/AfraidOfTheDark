package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation

/**
 * Base class for all AOTD spell power sources
 *
 * @param id The id of the spell power source
 * @param prerequisiteResearch The research required to use this component, or null if none is required
 * @constructor just calls super currently
 */
abstract class AOTDSpellPowerSource(id: ResourceLocation, prerequisiteResearch: Research? = null) : SpellPowerSource(id, prerequisiteResearch)
