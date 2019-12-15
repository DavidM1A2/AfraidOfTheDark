package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.*

/**
 * A static class containing all of our spell delivery method references for us
 */
object ModSpellDeliveryMethods
{
    val SELF = SpellDeliveryMethodSelf()
    val PROJECTILE = SpellDeliveryMethodProjectile()
    val AOE = SpellDeliveryMethodAOE()
    val LASER = SpellDeliveryMethodLaser()
    val DELAY = SpellDeliveryMethodDelay()

    // An array containing a list of spell delivery methods that AOTD adds
    val SPELL_DELIVERY_METHODS = arrayOf(
        SELF,
        PROJECTILE,
        AOE,
        LASER,
        DELAY
    )
}