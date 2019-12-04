package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Spin animation used by the splinter drone projectile
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelSping internal constructor(name: String?, fps: Float, totalFrames: Int, mode: Byte) : Channel(name, fps, totalFrames, mode)
{
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames()
    {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["body"] = Quaternion(0.083405174f, -0.372392f, 0.21956569f, 0.8978634f)
        frame0.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[0] = frame0
        val frame68 = KeyFrame()
        frame68.modelRenderersRotations["body"] = Quaternion(-0.18436967f, -0.70473063f, 0.5144693f, 0.4524201f)
        frame68.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[68] = frame68
        val frame81 = KeyFrame()
        frame81.modelRenderersRotations["body"] = Quaternion(-0.5597586f, 0.1650148f, 0.80388933f, 0.1149019f)
        frame81.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[81] = frame81
        val frame54 = KeyFrame()
        frame54.modelRenderersRotations["body"] = Quaternion(-0.5691636f, 0.43343335f, -0.5302429f, 0.45500636f)
        frame54.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[54] = frame54
        val frame99 = KeyFrame()
        frame99.modelRenderersRotations["body"] = Quaternion(-0.81073284f, 0.077038616f, 0.52543205f, 0.24637026f)
        frame99.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[99] = frame99
        val frame41 = KeyFrame()
        frame41.modelRenderersRotations["body"] = Quaternion(-0.14718205f, 0.24816278f, -0.38480353f, 0.8767433f)
        frame41.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[41] = frame41
        val frame26 = KeyFrame()
        frame26.modelRenderersRotations["body"] = Quaternion(-0.75852734f, 0.26399457f, -0.5914923f, 0.07127336f)
        frame26.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[26] = frame26
        val frame13 = KeyFrame()
        frame13.modelRenderersRotations["body"] = Quaternion(0.3145363f, -0.04252225f, 0.76564854f, 0.5595007f)
        frame13.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[13] = frame13
        val frame90 = KeyFrame()
        frame90.modelRenderersRotations["body"] = Quaternion(0.604039f, -0.683623f, -0.30258617f, 0.27611208f)
        frame90.modelRenderersTranslations["body"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[90] = frame90
    }
}