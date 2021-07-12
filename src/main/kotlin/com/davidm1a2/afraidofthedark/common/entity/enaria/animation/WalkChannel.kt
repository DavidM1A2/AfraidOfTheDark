package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f

/**
 * Walk animation used by enaria in the overworld
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class WalkChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame0.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame0.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[0] = frame0
        val frame58 = KeyFrame()
        frame58.modelRotations["body"] = Quaternion(0.0f, -0.0012466135f, 0.0f, 0.9999992f)
        frame58.modelRotations["leftarm"] = Quaternion(-0.0062269834f, 0.0f, 0.0f, 0.9999806f)
        frame58.modelRotations["rightleg"] = Quaternion(-0.027473606f, 0.0f, 0.0f, 0.9996225f)
        frame58.modelRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame58.modelRotations["rightarm"] = Quaternion(0.0062269834f, 0.0f, 0.0f, 0.9999806f)
        frame58.modelRotations["leftleg"] = Quaternion(0.027473606f, 0.0f, 0.0f, 0.9996225f)
        frame58.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame58.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame58.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame58.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame58.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame58.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[58] = frame58
        val frame45 = KeyFrame()
        frame45.modelRotations["body"] = Quaternion(0.0f, -0.017452406f, 0.0f, 0.9998477f)
        frame45.modelRotations["leftarm"] = Quaternion(-0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame45.modelRotations["rightleg"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame45.modelRotations["head"] = Quaternion(0.0f, 0.017452406f, 0.0f, 0.9998477f)
        frame45.modelRotations["rightarm"] = Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame45.modelRotations["leftleg"] = Quaternion(0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame45.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame45.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame45.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame45.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame45.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame45.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[45] = frame45
        val frame30 = KeyFrame()
        frame30.modelRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame30.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame30.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame30.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame30.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame30.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[30] = frame30
        val frame15 = KeyFrame()
        frame15.modelRotations["body"] = Quaternion(0.0f, 0.017452406f, 0.0f, 0.9998477f)
        frame15.modelRotations["leftarm"] = Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame15.modelRotations["rightleg"] = Quaternion(0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame15.modelRotations["head"] = Quaternion(0.0f, -0.017452406f, 0.0f, 0.9998477f)
        frame15.modelRotations["rightarm"] = Quaternion(-0.08715574f, 0.0f, 0.0f, 0.9961947f)
        frame15.modelRotations["leftleg"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame15.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame15.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame15.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame15.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame15.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame15.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[15] = frame15
    }
}