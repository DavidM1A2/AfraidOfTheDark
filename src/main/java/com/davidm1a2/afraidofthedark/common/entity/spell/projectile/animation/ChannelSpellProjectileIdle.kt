package com.davidm1a2.afraidofthedark.common.entity.spell.projectile.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Idle animation used by the projectile in flight
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelSpellProjectileIdle internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["Center"] = Quat4f(0.19001609f, 0.14813289f, -0.31068635f, 0.91946965f)
        frame0.modelRenderersTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[0] = frame0
        val frame17 = KeyFrame()
        frame17.modelRenderersRotations["Center"] = Quat4f(0.7796292f, 0.61504066f, 0.11790937f, -7.956326E-4f)
        frame17.modelRenderersTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[17] = frame17
        val frame39 = KeyFrame()
        frame39.modelRenderersRotations["Center"] = Quat4f(-0.5435533f, 0.589524f, -0.5917744f, -0.0825492f)
        frame39.modelRenderersTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[39] = frame39
        val frame59 = KeyFrame()
        frame59.modelRenderersRotations["Center"] = Quat4f(0.4235078f, -0.40492296f, 0.3203981f, 0.74432755f)
        frame59.modelRenderersTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[59] = frame59
    }
}