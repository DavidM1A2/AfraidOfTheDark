package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f


/**
 * Cast spell animation used by the enchanted frog
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelCast internal constructor(name: String?, fps: Float, totalFrames: Int, mode: Byte) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["frogLLeg"] = Quaternion(
            0.27059805f,
            0.27059805f,
            0.65328145f,
            0.65328145f
        )
        frame0.modelRenderersRotations["frogMouth"] = Quaternion(
            -0.08715574f,
            0.0f,
            0.0f,
            0.9961947f
        )
        frame0.modelRenderersRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.38268346f,
            0.0f,
            0.9238795f
        )
        frame0.modelRenderersRotations["frogHead"] = Quaternion(
            0.6427876f,
            0.0f,
            0.0f,
            0.76604444f
        )
        frame0.modelRenderersTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame0.modelRenderersTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame0.modelRenderersTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        frame0.modelRenderersTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[0] = frame0

        val frame20 = KeyFrame()
        frame20.modelRenderersRotations["frogMouth"] = Quaternion(
            0.043619387f,
            0.0f,
            0.0f,
            0.99904823f
        )
        frame20.modelRenderersRotations["frogHead"] = Quaternion(0.5f, 0.0f, 0.0f, 0.8660254f)
        frame20.modelRenderersTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame20.modelRenderersTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[20] = frame20

        val frame40 = KeyFrame()
        frame40.modelRenderersRotations["frogLLeg"] = Quaternion(
            0.35355338f,
            0.35355338f,
            0.6123724f,
            0.6123724f
        )
        frame40.modelRenderersRotations["frogRLeg"] = Quaternion(0.0f, -0.5f, 0.0f, 0.8660254f)
        frame40.modelRenderersRotations["frogHead"] = Quaternion(
            0.42261827f,
            0.0f,
            0.0f,
            0.90630776f
        )
        frame40.modelRenderersTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame40.modelRenderersTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        frame40.modelRenderersTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[40] = frame40

        val frame59 = KeyFrame()
        frame59.modelRenderersRotations["frogLLeg"] = Quaternion(
            0.27059805f,
            0.27059805f,
            0.65328145f,
            0.65328145f
        )
        frame59.modelRenderersRotations["frogMouth"] = Quaternion(
            -0.08715574f,
            0.0f,
            0.0f,
            0.9961947f
        )
        frame59.modelRenderersRotations["frogRLeg"] = Quaternion(
            0.0f,
            -0.38268346f,
            0.0f,
            0.9238795f
        )
        frame59.modelRenderersRotations["frogHead"] = Quaternion(
            0.6427876f,
            0.0f,
            0.0f,
            0.76604444f
        )
        frame59.modelRenderersTranslations["frogLLeg"] = Vector3f(4.0f, 0.0f, -3.0f)
        frame59.modelRenderersTranslations["frogMouth"] = Vector3f(-4.0f, 0.0f, 3.0f)
        frame59.modelRenderersTranslations["frogRLeg"] = Vector3f(-4.0f, 0.0f, -3.0f)
        frame59.modelRenderersTranslations["frogHead"] = Vector3f(-4.0f, 5.0f, 2.0f)
        keyFrames[59] = frame59
    }
}