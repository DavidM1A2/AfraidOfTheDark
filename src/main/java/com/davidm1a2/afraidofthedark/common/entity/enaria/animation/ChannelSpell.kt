package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Spell animation used by enaria in the overworld
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelSpell internal constructor(name: String?, fps: Float, totalFrames: Int, mode: Byte) :
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
        frame0.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[0] = frame0
        val frame80 = KeyFrame()
        frame80.modelRenderersRotations["body"] = Quaternion(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame80.modelRenderersTranslations["body"] = Vector3f(0.0f, 32.0f, 2.0f)
        keyFrames[80] = frame80
        val frame20 = KeyFrame()
        frame20.modelRenderersRotations["leftarm"] = Quaternion(-0.25f, 0.0669873f, -0.25f, 0.93301266f)
        frame20.modelRenderersRotations["rightarm"] = Quaternion(-0.25f, -0.0669873f, 0.25f, 0.93301266f)
        frame20.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRenderersTranslations["leftarm"] = Vector3f(3.0f, -2.0f, 0.0f)
        frame20.modelRenderersTranslations["rightarm"] = Vector3f(-3.0f, -2.0f, 0.0f)
        frame20.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[20] = frame20
        val frame100 = KeyFrame()
        frame100.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.70710677f, 0.70710677f)
        frame100.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, -0.70710677f, 0.70710677f)
        frame100.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame100.modelRenderersTranslations["leftarm"] = Vector3f(6.0f, -2.0f, 0.0f)
        frame100.modelRenderersTranslations["rightarm"] = Vector3f(-6.0f, -2.0f, 0.0f)
        frame100.modelRenderersTranslations["body"] = Vector3f(0.0f, 32.0f, 2.0f)
        keyFrames[100] = frame100
        val frame70 = KeyFrame()
        frame70.modelRenderersRotations["body"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame70.modelRenderersTranslations["body"] = Vector3f(0.0f, 32.0f, 2.0f)
        keyFrames[70] = frame70
        val frame90 = KeyFrame()
        frame90.modelRenderersRotations["body"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame90.modelRenderersTranslations["body"] = Vector3f(0.0f, 32.0f, 2.0f)
        keyFrames[90] = frame90
        val frame60 = KeyFrame()
        frame60.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.70710677f, 0.70710677f)
        frame60.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, -0.70710677f, 0.70710677f)
        frame60.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame60.modelRenderersTranslations["leftarm"] = Vector3f(6.0f, -2.0f, 0.0f)
        frame60.modelRenderersTranslations["rightarm"] = Vector3f(-6.0f, -2.0f, 0.0f)
        frame60.modelRenderersTranslations["body"] = Vector3f(0.0f, 32.0f, 2.0f)
        keyFrames[60] = frame60
        val frame110 = KeyFrame()
        frame110.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame110.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame110.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame110.modelRenderersTranslations["leftarm"] = Vector3f(6.0f, -2.0f, 0.0f)
        frame110.modelRenderersTranslations["rightarm"] = Vector3f(-6.0f, -2.0f, 0.0f)
        frame110.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[110] = frame110
    }
}