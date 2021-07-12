package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f

/**
 * Arm throw animation used by enaria in the overworld
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ArmthrowChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[0] = frame0
        val frame50 = KeyFrame()
        frame50.modelRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame50.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[50] = frame50
        val frame35 = KeyFrame()
        frame35.modelRotations["leftarm"] = Quaternion(0.4477183f, 0.5392792f, 0.54877454f, 0.45560145f)
        frame35.modelRotations["rightarm"] = Quaternion(-0.7302104f, -0.12133739f, -0.161128f, 0.6527693f)
        frame35.modelTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 22.0f)
        frame35.modelTranslations["rightarm"] = Vector3f(-3.0f, -1.0f, 2.0f)
        keyFrames[35] = frame35
        val frame20 = KeyFrame()
        frame20.modelRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRotations["rightarm"] = Quaternion(-0.3671902f, 0.59756726f, 0.3179583f, 0.63795555f)
        frame20.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame20.modelTranslations["rightarm"] = Vector3f(-3.0f, -2.5f, 0.0f)
        keyFrames[20] = frame20
        val frame23 = KeyFrame()
        frame23.modelRotations["leftarm"] = Quaternion(0.0f, 0.0f, -0.050592944f, 0.99871933f)
        frame23.modelRotations["rightarm"] = Quaternion(-0.36719024f, 0.59756726f, 0.3179583f, 0.63795555f)
        frame23.modelTranslations["leftarm"] = Vector3f(4.2f, -2.5f, 0.0f)
        frame23.modelTranslations["rightarm"] = Vector3f(-3.0f, -3.0f, 0.0f)
        keyFrames[23] = frame23
        val frame56 = KeyFrame()
        frame56.modelRotations["leftarm"] = Quaternion(-0.10266903f, 0.028632134f, 0.6987209f, 0.7074096f)
        frame56.modelTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 116.9f)
        keyFrames[56] = frame56
        val frame42 = KeyFrame()
        frame42.modelRotations["leftarm"] = Quaternion(-0.7024243f, -0.7052553f, -0.085041374f, 0.04453233f)
        frame42.modelTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 53.6f)
        keyFrames[42] = frame42
        val frame60 = KeyFrame()
        frame60.modelRotations["leftarm"] = Quaternion(0.45560145f, 0.54877454f, 0.5392792f, 0.4477183f)
        frame60.modelTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 135.0f)
        keyFrames[60] = frame60
        val frame47 = KeyFrame()
        frame47.modelRotations["leftarm"] = Quaternion(-0.5366766f, -0.44275555f, 0.46048072f, 0.55127424f)
        frame47.modelTranslations["leftarm"] = Vector3f(-6.0f, -3.5f, 76.2f)
        keyFrames[47] = frame47
    }
}