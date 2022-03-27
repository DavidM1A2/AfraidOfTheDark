package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.AOESpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.ChainSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.DelaySpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.ImbueSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.LaserSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.ProjectileSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.RotateSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.SelfSpellDeliveryMethod

/**
 * A static class containing all of our spell delivery method references for us
 */
object ModSpellDeliveryMethods {
    val SELF = SelfSpellDeliveryMethod()
    val PROJECTILE = ProjectileSpellDeliveryMethod()
    val AOE = AOESpellDeliveryMethod()
    val LASER = LaserSpellDeliveryMethod()
    val DELAY = DelaySpellDeliveryMethod()
    val ROTATE = RotateSpellDeliveryMethod()
    val CHAIN = ChainSpellDeliveryMethod()
    val IMBUE = ImbueSpellDeliveryMethod()

    // An array containing a list of spell delivery methods that AOTD adds
    val SPELL_DELIVERY_METHODS = arrayOf(
        SELF,
        PROJECTILE,
        AOE,
        LASER,
        DELAY,
        ROTATE,
        CHAIN,
        IMBUE
    )
}