/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f

/**
 * Walk animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class WalkChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame0.modelRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame0.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame0.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame0.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[0] = frame0
        val frame39 = KeyFrame()
        frame39.modelRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame39.modelRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame39.modelRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame39.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame39.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame39.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame39.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[39] = frame39
        val frame20 = KeyFrame()
        frame20.modelRotations["rightarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame20.modelRotations["rightleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRotations["leftleg"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame20.modelRotations["leftarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame20.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame20.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame20.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame20.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[20] = frame20
        val frame10 = KeyFrame()
        frame10.modelRotations["rightarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame10.modelRotations["rightleg"] = Quaternion(-0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame10.modelRotations["leftleg"] = Quaternion(0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame10.modelRotations["leftarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame10.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame10.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame10.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame10.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[10] = frame10
        val frame29 = KeyFrame()
        frame29.modelRotations["rightarm"] = Quaternion(-0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame29.modelRotations["rightleg"] = Quaternion(0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame29.modelRotations["leftleg"] = Quaternion(-0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame29.modelRotations["leftarm"] = Quaternion(-0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame29.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame29.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame29.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame29.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[29] = frame29
    }
}