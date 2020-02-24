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
 * Walk animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelWalk internal constructor(name: String?, fps: Float, totalFrames: Int, mode: Byte) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame0.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame0.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame0.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame0.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[0] = frame0
        val frame39 = KeyFrame()
        frame39.modelRenderersRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame39.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRenderersRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame39.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame39.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame39.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame39.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[39] = frame39
        val frame20 = KeyFrame()
        frame20.modelRenderersRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame20.modelRenderersRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRenderersRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRenderersRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame20.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame20.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame20.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame20.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[20] = frame20
        val frame10 = KeyFrame()
        frame10.modelRenderersRotations["rightarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame10.modelRenderersRotations["rightleg"] = Quaternion(-0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame10.modelRenderersRotations["leftleg"] = Quaternion(0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame10.modelRenderersRotations["leftarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame10.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame10.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame10.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame10.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[10] = frame10
        val frame29 = KeyFrame()
        frame29.modelRenderersRotations["rightarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame29.modelRenderersRotations["rightleg"] = Quaternion(0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame29.modelRenderersRotations["leftleg"] = Quaternion(-0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame29.modelRenderersRotations["leftarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame29.modelRenderersTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame29.modelRenderersTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame29.modelRenderersTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame29.modelRenderersTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[29] = frame29
    }
}