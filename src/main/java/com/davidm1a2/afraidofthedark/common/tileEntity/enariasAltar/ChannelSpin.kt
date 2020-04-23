package com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f


/**
 * Spin animation used by enaria's altar
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelSpin(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["spike2"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame0.modelRotations["spike4"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame0.modelRotations["spike1"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame0.modelRotations["spike3"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame0.modelTranslations["spike2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["spike4"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["spike1"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["spike3"] = Vector3f(0.0f, 5.0f, 0.0f)
        keyFrames[0] = frame0

        val frame30 = KeyFrame()
        frame30.modelRotations["spike2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["spike4"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["spike1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["spike3"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelTranslations["spike2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame30.modelTranslations["spike4"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame30.modelTranslations["spike1"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame30.modelTranslations["spike3"] = Vector3f(0.0f, 5.0f, 0.0f)
        keyFrames[30] = frame30

        val frame59 = KeyFrame()
        frame59.modelRotations["spike2"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame59.modelRotations["spike4"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame59.modelRotations["spike3"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame59.modelRotations["spike1"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame59.modelTranslations["spike2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame59.modelTranslations["spike4"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame59.modelTranslations["spike3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame59.modelTranslations["spike1"] = Vector3f(0.0f, 5.0f, 0.0f)
        keyFrames[59] = frame59
    }
}