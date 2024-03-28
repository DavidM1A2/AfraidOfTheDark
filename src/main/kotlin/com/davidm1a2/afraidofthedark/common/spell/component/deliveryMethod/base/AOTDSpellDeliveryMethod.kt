package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.resources.ResourceLocation

abstract class AOTDSpellDeliveryMethod(name: String, prerequisiteResearch: Research? = null) : SpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, name), prerequisiteResearch)