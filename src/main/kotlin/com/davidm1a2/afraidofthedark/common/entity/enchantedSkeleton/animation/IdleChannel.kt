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
 * Idle animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class IdleChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["rightarm"] = Quaternion(-0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame0.modelRotations["heart"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["leftarm"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame0.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["heart"] = Vector3f(0.0f, -3.0f, 0.0f)
        frame0.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[0] = frame0
        val frame19 = KeyFrame()
        frame19.modelRotations["rightarm"] = Quaternion(-0.34202012f, 0.0f, 0.0f, 0.9396926f)
        frame19.modelRotations["heart"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame19.modelRotations["leftarm"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame19.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame19.modelTranslations["heart"] = Vector3f(0.0f, -3.0f, 0.0f)
        frame19.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[19] = frame19
        val frame9 = KeyFrame()
        frame9.modelRotations["rightarm"] = Quaternion(-0.35836795f, 0.0f, 0.0f, 0.9335804f)
        frame9.modelRotations["heart"] = Quaternion(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame9.modelRotations["leftarm"] = Quaternion(-0.39874908f, 0.0f, 0.0f, 0.9170601f)
        frame9.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame9.modelTranslations["heart"] = Vector3f(0.0f, -3.0f, 0.0f)
        frame9.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        keyFrames[9] = frame9
    }
}