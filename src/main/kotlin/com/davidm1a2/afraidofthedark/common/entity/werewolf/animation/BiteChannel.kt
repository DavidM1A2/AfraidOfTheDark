package com.davidm1a2.afraidofthedark.common.entity.werewolf.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Bite animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class BiteChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["Head"] = Quat4f(0.0784591f, 0.0f, 0.0f, 0.9969173f)
        frame0.modelTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        keyFrames[0] = frame0
        val frame20 = KeyFrame()
        frame20.modelRotations["SnoutLower"] = Quat4f(0.043619387f, 0.0f, 0.0f, 0.99904823f)
        frame20.modelRotations["Head"] = Quat4f(0.077267125f, -0.01362428f, -0.17311287f, 0.9817719f)
        frame20.modelTranslations["SnoutLower"] = Vector3f(0.0f, -1.0f, 8.0f)
        frame20.modelTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        keyFrames[20] = frame20
        val frame39 = KeyFrame()
        frame39.modelRotations["SnoutLower"] = Quat4f(0.17364818f, 0.0f, 0.0f, 0.9848077f)
        frame39.modelRotations["Head"] = Quat4f(0.0784591f, 0.0f, 0.0f, 0.9969173f)
        frame39.modelTranslations["SnoutLower"] = Vector3f(0.0f, -1.0f, 8.0f)
        frame39.modelTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        keyFrames[39] = frame39
        val frame14 = KeyFrame()
        frame14.modelRotations["SnoutLower"] = Quat4f(0.42261827f, 0.0f, 0.0f, 0.90630776f)
        frame14.modelTranslations["SnoutLower"] = Vector3f(0.0f, -1.0f, 8.0f)
        keyFrames[14] = frame14
    }
}