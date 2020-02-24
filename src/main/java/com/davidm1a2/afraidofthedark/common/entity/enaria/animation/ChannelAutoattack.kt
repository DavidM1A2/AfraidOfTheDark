package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Auto attack animation used by enaria in the overworld
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelAutoattack internal constructor(name: String, fps: Float, totalFrames: Int, mode: Byte) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame0.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[0] = frame0
        val frame50 = KeyFrame()
        frame50.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame50.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame50.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame50.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame50.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame50.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[50] = frame50
        val frame30 = KeyFrame()
        frame30.modelRenderersRotations["leftarm"] = Quaternion(-0.6889273f, -0.17649558f, 0.13222496f, 0.69046724f)
        frame30.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.06975647f, 0.9975641f)
        frame30.modelRenderersRotations["rightarm"] = Quaternion(-0.6543772f, -0.0049159825f, 0.17740688f, 0.7350463f)
        frame30.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRenderersTranslations["leftarm"] = Vector3f(2.0f, -2.5f, 0.0f)
        frame30.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame30.modelRenderersTranslations["head"] = Vector3f(0.0f, -0.5f, 0.0f)
        frame30.modelRenderersTranslations["rightarm"] = Vector3f(-2.0f, -2.0f, 0.0f)
        frame30.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        keyFrames[30] = frame30
    }
}