package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Activate animation used by the splinter drone
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelActivate internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["BottomPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["TopPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame0.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, -3.0f)
        frame0.modelRenderersTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame0.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 11.0f, 0.0f)
        frame0.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, -11.0f, 0.0f)
        frame0.modelRenderersTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar2"] = Vector3f(-3.5f, 0.0f, -3.5f)
        frame0.modelRenderersTranslations["Pillar6"] = Vector3f(3.5f, 0.0f, 3.5f)
        frame0.modelRenderersTranslations["Pillar7"] = Vector3f(3.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar8"] = Vector3f(3.5f, 0.0f, -3.5f)
        frame0.modelRenderersTranslations["Pillar4"] = Vector3f(-3.5f, 0.0f, 3.5f)
        frame0.modelRenderersTranslations["Pillar3"] = Vector3f(-3.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 3.0f)
        keyFrames[0] = frame0
        val frame1 = KeyFrame()
        frame1.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRenderersRotations["BottomPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRenderersRotations["TopPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame1.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame1.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame1.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame1.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame1.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame1.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame1.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, -3.0f)
        frame1.modelRenderersTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame1.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 11.0f, 0.0f)
        frame1.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, -11.0f, 0.0f)
        frame1.modelRenderersTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame1.modelRenderersTranslations["Pillar2"] = Vector3f(-3.5f, 0.0f, -3.5f)
        frame1.modelRenderersTranslations["Pillar6"] = Vector3f(3.5f, 0.0f, 3.5f)
        frame1.modelRenderersTranslations["Pillar7"] = Vector3f(3.0f, 0.0f, 0.0f)
        frame1.modelRenderersTranslations["Pillar8"] = Vector3f(3.5f, 0.0f, -3.5f)
        frame1.modelRenderersTranslations["Pillar4"] = Vector3f(-3.5f, 0.0f, 3.5f)
        frame1.modelRenderersTranslations["Pillar3"] = Vector3f(-3.0f, 0.0f, 0.0f)
        frame1.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 3.0f)
        keyFrames[1] = frame1
        val frame86 = KeyFrame()
        frame86.modelRenderersRotations["Sphere2"] = Quaternion(-1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame86.modelRenderersRotations["Sphere1"] = Quaternion(-1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame86.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame86.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[86] = frame86
        val frame99 = KeyFrame()
        frame99.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["BottomPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["TopPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame99.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame99.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[99] = frame99
        val frame24 = KeyFrame()
        frame24.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRenderersRotations["TopPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame24.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, -3.0f)
        frame24.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar2"] = Vector3f(-3.5f, 0.0f, -3.5f)
        frame24.modelRenderersTranslations["Pillar6"] = Vector3f(3.5f, 0.0f, 3.5f)
        frame24.modelRenderersTranslations["Pillar7"] = Vector3f(3.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar8"] = Vector3f(3.5f, 0.0f, -3.5f)
        frame24.modelRenderersTranslations["Pillar4"] = Vector3f(-3.5f, 0.0f, 3.5f)
        frame24.modelRenderersTranslations["Pillar3"] = Vector3f(-3.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 3.0f)
        keyFrames[24] = frame24
        val frame73 = KeyFrame()
        frame73.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRenderersRotations["BottomPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRenderersRotations["TopPlate"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame73.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame73.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame73.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame73.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame73.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame73.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame73.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame73.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[73] = frame73
    }
}