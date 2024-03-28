package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.resources.ResourceLocation

abstract class AOTDSpellPowerSource<T>(name: String, prerequisiteResearch: Research? = null) : SpellPowerSource<T>(ResourceLocation(Constants.MOD_ID, name), prerequisiteResearch)
