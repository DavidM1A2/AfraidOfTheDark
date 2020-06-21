package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import net.minecraft.util.ResourceLocation

/**
 * Base class for all AOTD delivery methods
 *
 * @constructor calls super
 * @param id The ID of this delivery method entry
 */
abstract class AOTDSpellDeliveryMethod(id: ResourceLocation) : SpellDeliveryMethod(id)
