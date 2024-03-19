package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f


/**
 * Walk/hop animation used by the enchanted frog
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class HopChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    init {
        val frame0 = KeyFrame()
        frame0.modelRotations["frogBody"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
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
        frame0.modelTranslations["frogBody"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame0.modelTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame0.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        keyFrames[0] = frame0

        val frame20 = KeyFrame()
        frame20.modelRotations["frogBody"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRotations["frogLLeg"] = Quaternion(
            0.29883623f,
            0.29883623f,
            0.6408563f,
            0.6408563f
        )
        frame20.modelRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.42261827f,
            0.0f,
            0.90630776f
        )
        frame20.modelTranslations["frogBody"] = Vector3f(0.0f, 3.0f, 0.0f)
        frame20.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame20.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        keyFrames[20] = frame20

        val frame40 = KeyFrame()
        frame40.modelRotations["frogBody"] = Quaternion(
            0.06975647f,
            0.0f,
            0.0f,
            0.9975641f
        )
        frame40.modelRotations["frogLLeg"] = Quaternion(
            0.4055798f,
            0.4055798f,
            0.579228f,
            0.579228f
        )
        frame40.modelRotations["frogMouth"] = Quaternion(
            0.043619387f,
            0.0f,
            0.0f,
            0.99904823f
        )
        frame40.modelRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.57357645f,
            0.0f,
            0.81915206f
        )
        frame40.modelTranslations["frogBody"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame40.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame40.modelTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame40.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        keyFrames[40] = frame40

        val frame60 = KeyFrame()
        frame60.modelRotations["frogBody"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame60.modelRotations["frogLLeg"] = Quaternion(
            0.1830127f,
            0.1830127f,
            0.68301266f,
            0.68301266f
        )
        frame60.modelRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.25881904f,
            0.0f,
            0.9659258f
        )
        frame60.modelTranslations["frogBody"] = Vector3f(0.0f, 3.0f, 0.0f)
        frame60.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame60.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        keyFrames[60] = frame60

        val frame79 = KeyFrame()
        frame79.modelRotations["frogBody"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame79.modelRotations["frogLLeg"] = Quaternion(
            0.27059805f,
            0.27059805f,
            0.65328145f,
            0.65328145f
        )
        frame79.modelRotations["frogMouth"] = Quaternion(
            -0.08715574f,
            0.0f,
            0.0f,
            0.9961947f
        )
        frame79.modelRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.38268346f,
            0.0f,
            0.9238795f
        )
        frame79.modelTranslations["frogBody"] = Vector3f(0.0f, 1.0f, 0.0f)
        frame79.modelTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame79.modelTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame79.modelTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        keyFrames[79] = frame79
    }
}