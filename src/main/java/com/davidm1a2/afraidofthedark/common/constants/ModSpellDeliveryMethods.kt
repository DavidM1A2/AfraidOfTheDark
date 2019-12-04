package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.*
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry
import net.minecraft.util.ResourceLocation
import java.util.function.Supplier

/**
 * A static class containing all of our spell delivery method references for us
 */
object ModSpellDeliveryMethods
{
    val SELF = SpellDeliveryMethodEntry(ResourceLocation(Constants.MOD_ID, "self")) { SpellDeliveryMethodSelf() }
    val PROJECTILE = SpellDeliveryMethodEntry(ResourceLocation(Constants.MOD_ID, "projectile")) { SpellDeliveryMethodProjectile() }
    val AOE = SpellDeliveryMethodEntry(ResourceLocation(Constants.MOD_ID, "aoe")) { SpellDeliveryMethodAOE() }
    val LASER = SpellDeliveryMethodEntry(ResourceLocation(Constants.MOD_ID, "laser")) { SpellDeliveryMethodLaser() }
    val DELAY = SpellDeliveryMethodEntry(ResourceLocation(Constants.MOD_ID, "delay")) { SpellDeliveryMethodDelay() }

    // An array containing a list of spell delivery methods that AOTD adds
    val SPELL_DELIVERY_METHODS = arrayOf(
            SELF,
            PROJECTILE,
            AOE,
            LASER,
            DELAY
    )
}