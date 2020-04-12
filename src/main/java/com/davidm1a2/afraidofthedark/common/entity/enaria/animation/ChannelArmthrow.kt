package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Arm throw animation used by enaria in the overworld
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelArmthrow internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[0] = frame0
        val frame50 = KeyFrame()
        frame50.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame50.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[50] = frame50
        val frame35 = KeyFrame()
        frame35.modelRenderersRotations["leftarm"] = Quaternion(0.4477183f, 0.5392792f, 0.54877454f, 0.45560145f)
        frame35.modelRenderersRotations["rightarm"] = Quaternion(-0.7302104f, -0.12133739f, -0.161128f, 0.6527693f)
        frame35.modelRenderersTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 22.0f)
        frame35.modelRenderersTranslations["rightarm"] = Vector3f(-3.0f, -1.0f, 2.0f)
        keyFrames[35] = frame35
        val frame20 = KeyFrame()
        frame20.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRenderersRotations["rightarm"] = Quaternion(-0.3671902f, 0.59756726f, 0.3179583f, 0.63795555f)
        frame20.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame20.modelRenderersTranslations["rightarm"] = Vector3f(-3.0f, -2.5f, 0.0f)
        keyFrames[20] = frame20
        val frame23 = KeyFrame()
        frame23.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, -0.050592944f, 0.99871933f)
        frame23.modelRenderersRotations["rightarm"] = Quaternion(-0.36719024f, 0.59756726f, 0.3179583f, 0.63795555f)
        frame23.modelRenderersTranslations["leftarm"] = Vector3f(4.2f, -2.5f, 0.0f)
        frame23.modelRenderersTranslations["rightarm"] = Vector3f(-3.0f, -3.0f, 0.0f)
        keyFrames[23] = frame23
        val frame56 = KeyFrame()
        frame56.modelRenderersRotations["leftarm"] = Quaternion(-0.10266903f, 0.028632134f, 0.6987209f, 0.7074096f)
        frame56.modelRenderersTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 116.9f)
        keyFrames[56] = frame56
        val frame42 = KeyFrame()
        frame42.modelRenderersRotations["leftarm"] = Quaternion(-0.7024243f, -0.7052553f, -0.085041374f, 0.04453233f)
        frame42.modelRenderersTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 53.6f)
        keyFrames[42] = frame42
        val frame60 = KeyFrame()
        frame60.modelRenderersRotations["leftarm"] = Quaternion(0.45560145f, 0.54877454f, 0.5392792f, 0.4477183f)
        frame60.modelRenderersTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 135.0f)
        keyFrames[60] = frame60
        val frame47 = KeyFrame()
        frame47.modelRenderersRotations["leftarm"] = Quaternion(-0.5366766f, -0.44275555f, 0.46048072f, 0.55127424f)
        frame47.modelRenderersTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 76.2f)
        keyFrames[47] = frame47
    }
}