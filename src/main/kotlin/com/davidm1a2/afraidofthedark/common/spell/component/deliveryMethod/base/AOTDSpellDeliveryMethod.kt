package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation

/**
 * Base class for all AOTD delivery methods
 *
 * @constructor calls super
 * @param id The ID of this delivery method entry
 * @param prerequisiteResearch The research required to use this component, or null if none is required
 */
abstract class AOTDSpellDeliveryMethod(id: ResourceLocation, prerequisiteResearch: Research? = null) : SpellDeliveryMethod(id, prerequisiteResearch)