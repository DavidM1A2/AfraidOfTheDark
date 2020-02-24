/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Spawn animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelSpawn internal constructor(name: String?, fps: Float, totalFrames: Int, mode: Byte) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["rightarm"] = Quaternion(0.1830127f, -0.1830127f, -0.68301266f, 0.68301266f)
        frame0.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, -0.70710677f, 0.70710677f)
        frame0.modelRenderersRotations["leftleg"] = Quaternion(-0.70710677f, 0.0f, 0.0f, 0.70710677f)
        frame0.modelRenderersRotations["head"] = Quaternion(-0.28985932f, 0.28985932f, 0.6449663f, -0.6449663f)
        frame0.modelRenderersRotations["heart"] = Quaternion(0.0f, -0.7906896f, 0.0f, 0.61221725f)
        frame0.modelRenderersRotations["leftarm"] = Quaternion(0.47314674f, -0.47314674f, -0.5254827f, 0.5254827f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-5.0f, -24.0f, 9.0f)
        frame0.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -23.0f, 0.0f)
        frame0.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -23.0f, 7.0f)
        frame0.modelRenderersTranslations["head"] = Vector3f(10.0f, -20.0f, -13.0f)
        frame0.modelRenderersTranslations["heart"] = Vector3f(0.0f, -22.0f, 0.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -22.0f, 0.0f)
        keyFrames[0] = frame0
        val frame17 = KeyFrame()
        frame17.modelRenderersRotations["leftleg"] = Quaternion(-0.70710677f, 0.0f, 0.0f, 0.70710677f)
        frame17.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -23.0f, -0.100000024f)
        keyFrames[17] = frame17
        val frame34 = KeyFrame()
        frame34.modelRenderersRotations["head"] = Quaternion(-0.7247734f, 0.0f, 0.0f, 0.6889873f)
        frame34.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[34] = frame34
        val frame33 = KeyFrame()
        frame33.modelRenderersRotations["rightarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame33.modelRenderersRotations["leftarm"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame33.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame33.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[33] = frame33
        val frame21 = KeyFrame()
        frame21.modelRenderersRotations["heart"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame21.modelRenderersTranslations["heart"] = Vector3f(0.0f, -3.0f, 0.0f)
        keyFrames[21] = frame21
        val frame39 = KeyFrame()
        frame39.modelRenderersRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame39.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRenderersRotations["head"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRenderersRotations["heart"] = Quaternion(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame39.modelRenderersRotations["body"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRenderersRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame39.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame39.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame39.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame39.modelRenderersTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame39.modelRenderersTranslations["heart"] = Vector3f(0.0f, -3.0f, 0.0f)
        frame39.modelRenderersTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        frame39.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[39] = frame39
        val frame31 = KeyFrame()
        frame31.modelRenderersRotations["heart"] = Quaternion(0.0f, 0.88253754f, 0.0f, 0.4702419f)
        frame31.modelRenderersTranslations["heart"] = Vector3f(0.0f, -3.0f, 0.0f)
        keyFrames[31] = frame31
    }
}