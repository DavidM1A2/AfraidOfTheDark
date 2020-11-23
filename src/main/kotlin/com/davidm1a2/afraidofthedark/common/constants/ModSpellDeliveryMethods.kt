package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.*

/**
 * A static class containing all of our spell delivery method references for us
 */
object ModSpellDeliveryMethods {
    val SELF = SelfSpellDeliveryMethod()
    val PROJECTILE = ProjectileSpellDeliveryMethod()
    val AOE = AOESpellDeliveryMethod()
    val LASER = LaserSpellDeliveryMethod()
    val DELAY = DelaySpellDeliveryMethod()

    // An array containing a list of spell delivery methods that AOTD adds
    val SPELL_DELIVERY_METHODS = arrayOf(
        SELF,
        PROJECTILE,
        AOE,
        LASER,
        DELAY
    )
}