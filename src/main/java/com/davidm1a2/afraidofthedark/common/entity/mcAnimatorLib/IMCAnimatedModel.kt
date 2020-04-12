package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler

/**
 * This class was provided by the MC animator library, defines an interface to be used by all MC animated models
 */
interface IMCAnimatedModel {
    /**
     * @return the animation handler for this model
     */
    fun getAnimationHandler(): AnimationHandler
}
