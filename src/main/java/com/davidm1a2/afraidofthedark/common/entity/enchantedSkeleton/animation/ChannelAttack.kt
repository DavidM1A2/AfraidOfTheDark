/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Attack animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelAttack internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["rightarm"] = Quat4f(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame0.modelRenderersRotations["leftarm"] = Quat4f(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[0] = frame0
        val frame19 = KeyFrame()
        frame19.modelRenderersRotations["rightarm"] = Quat4f(-0.9542403f, 0.0f, 0.0f, 0.2990408f)
        frame19.modelRenderersRotations["leftarm"] = Quat4f(-0.9378889f, 0.0f, 0.0f, 0.3469357f)
        frame19.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame19.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[19] = frame19
    }
}