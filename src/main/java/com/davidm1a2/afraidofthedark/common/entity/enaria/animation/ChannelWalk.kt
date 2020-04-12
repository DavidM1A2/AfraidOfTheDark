package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Walk animation used by enaria in the overworld
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelWalk internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame0.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[0] = frame0
        val frame58 = KeyFrame()
        frame58.modelRenderersRotations["body"] = Quaternion(0.0f, -0.0012466135f, 0.0f, 0.9999992f)
        frame58.modelRenderersRotations["leftarm"] = Quaternion(-0.0062269834f, 0.0f, 0.0f, 0.9999806f)
        frame58.modelRenderersRotations["rightleg"] = Quaternion(-0.027473606f, 0.0f, 0.0f, 0.9996225f)
        frame58.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame58.modelRenderersRotations["rightarm"] = Quaternion(0.0062269834f, 0.0f, 0.0f, 0.9999806f)
        frame58.modelRenderersRotations["leftleg"] = Quaternion(0.027473606f, 0.0f, 0.0f, 0.9996225f)
        frame58.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame58.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame58.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame58.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame58.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame58.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[58] = frame58
        val frame45 = KeyFrame()
        frame45.modelRenderersRotations["body"] = Quaternion(0.0f, -0.017452406f, 0.0f, 0.9998477f)
        frame45.modelRenderersRotations["leftarm"] = Quaternion(-0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame45.modelRenderersRotations["rightleg"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame45.modelRenderersRotations["head"] = Quaternion(0.0f, 0.017452406f, 0.0f, 0.9998477f)
        frame45.modelRenderersRotations["rightarm"] = Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame45.modelRenderersRotations["leftleg"] = Quaternion(0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame45.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame45.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame45.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame45.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame45.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame45.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[45] = frame45
        val frame30 = KeyFrame()
        frame30.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame30.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame30.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame30.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame30.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame30.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[30] = frame30
        val frame15 = KeyFrame()
        frame15.modelRenderersRotations["body"] = Quaternion(0.0f, 0.017452406f, 0.0f, 0.9998477f)
        frame15.modelRenderersRotations["leftarm"] = Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame15.modelRenderersRotations["rightleg"] = Quaternion(0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame15.modelRenderersRotations["head"] = Quaternion(0.0f, -0.017452406f, 0.0f, 0.9998477f)
        frame15.modelRenderersRotations["rightarm"] = Quaternion(-0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame15.modelRenderersRotations["leftleg"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame15.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame15.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame15.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame15.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame15.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame15.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[15] = frame15
    }
}