package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f


/**
 * Cast spell animation used by the enchanted frog
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class CastChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    init {
        val frame0 = KeyFrame()
        frame0.modelRotations["frogLLeg"] = Quaternion(
            0.27059805f,
            0.27059805f,
            0.65328145f,
            0.65328145f
        )
        frame0.modelRotations["frogMouth"] = Quaternion(
            -0.08715574f,
            0.0f,
            0.0f,
            0.9961947f
        )
        frame0.modelRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.38268346f,
            0.0f,
            0.9238795f
        )
        frame0.modelRotations["frogHead"] = Quaternion(
            0.6427876f,
            0.0f,
            0.0f,
            0.76604444f
        )
        frame0.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame0.modelTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame0.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        frame0.modelTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[0] = frame0

        val frame20 = KeyFrame()
        frame20.modelRotations["frogMouth"] = Quaternion(
            0.043619387f,
            0.0f,
            0.0f,
            0.99904823f
        )
        frame20.modelRotations["frogHead"] = Quaternion(0.5f, 0.0f, 0.0f, 0.8660254f)
        frame20.modelTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame20.modelTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[20] = frame20

        val frame40 = KeyFrame()
        frame40.modelRotations["frogLLeg"] = Quaternion(
            0.35355338f,
            0.35355338f,
            0.6123724f,
            0.6123724f
        )
        frame40.modelRotations["frogRLeg"] = Quaternion(0.0f, -0.5f, 0.0f, 0.8660254f)
        frame40.modelRotations["frogHead"] = Quaternion(
            0.42261827f,
            0.0f,
            0.0f,
            0.90630776f
        )
        frame40.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame40.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        frame40.modelTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[40] = frame40

        val frame59 = KeyFrame()
        frame59.modelRotations["frogLLeg"] = Quaternion(
            0.27059805f,
            0.27059805f,
            0.65328145f,
            0.65328145f
        )
        frame59.modelRotations["frogMouth"] = Quaternion(
            -0.08715574f,
            0.0f,
            0.0f,
            0.9961947f
        )
        frame59.modelRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.38268346f,
            0.0f,
            0.9238795f
        )
        frame59.modelRotations["frogHead"] = Quaternion(
            0.6427876f,
            0.0f,
            0.0f,
            0.76604444f
        )
        frame59.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame59.modelTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame59.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        frame59.modelTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[59] = frame59
    }
}