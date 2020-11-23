package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Activate animation used by the splinter drone
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ActivateActivate internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["BottomPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["TopPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRotations["Pillar5"] = Quat4f(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame0.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, -3.0f)
        frame0.modelTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame0.modelTranslations["Sphere2"] = Vector3f(0.0f, 11.0f, 0.0f)
        frame0.modelTranslations["Sphere1"] = Vector3f(0.0f, -11.0f, 0.0f)
        frame0.modelTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame0.modelTranslations["Pillar2"] = Vector3f(-3.5f, 0.0f, -3.5f)
        frame0.modelTranslations["Pillar6"] = Vector3f(3.5f, 0.0f, 3.5f)
        frame0.modelTranslations["Pillar7"] = Vector3f(3.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar8"] = Vector3f(3.5f, 0.0f, -3.5f)
        frame0.modelTranslations["Pillar4"] = Vector3f(-3.5f, 0.0f, 3.5f)
        frame0.modelTranslations["Pillar3"] = Vector3f(-3.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 3.0f)
        keyFrames[0] = frame0
        val frame1 = KeyFrame()
        frame1.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRotations["BottomPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRotations["TopPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame1.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame1.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame1.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame1.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame1.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame1.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame1.modelRotations["Pillar5"] = Quat4f(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame1.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, -3.0f)
        frame1.modelTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame1.modelTranslations["Sphere2"] = Vector3f(0.0f, 11.0f, 0.0f)
        frame1.modelTranslations["Sphere1"] = Vector3f(0.0f, -11.0f, 0.0f)
        frame1.modelTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame1.modelTranslations["Pillar2"] = Vector3f(-3.5f, 0.0f, -3.5f)
        frame1.modelTranslations["Pillar6"] = Vector3f(3.5f, 0.0f, 3.5f)
        frame1.modelTranslations["Pillar7"] = Vector3f(3.0f, 0.0f, 0.0f)
        frame1.modelTranslations["Pillar8"] = Vector3f(3.5f, 0.0f, -3.5f)
        frame1.modelTranslations["Pillar4"] = Vector3f(-3.5f, 0.0f, 3.5f)
        frame1.modelTranslations["Pillar3"] = Vector3f(-3.0f, 0.0f, 0.0f)
        frame1.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 3.0f)
        keyFrames[1] = frame1
        val frame86 = KeyFrame()
        frame86.modelRotations["Sphere2"] = Quat4f(-1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame86.modelRotations["Sphere1"] = Quat4f(-1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame86.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame86.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[86] = frame86
        val frame99 = KeyFrame()
        frame99.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["BottomPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["TopPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRotations["Pillar5"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame99.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame99.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame99.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[99] = frame99
        val frame24 = KeyFrame()
        frame24.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRotations["TopPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRotations["Pillar5"] = Quat4f(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame24.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, -3.0f)
        frame24.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame24.modelTranslations["Pillar2"] = Vector3f(-3.5f, 0.0f, -3.5f)
        frame24.modelTranslations["Pillar6"] = Vector3f(3.5f, 0.0f, 3.5f)
        frame24.modelTranslations["Pillar7"] = Vector3f(3.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar8"] = Vector3f(3.5f, 0.0f, -3.5f)
        frame24.modelTranslations["Pillar4"] = Vector3f(-3.5f, 0.0f, 3.5f)
        frame24.modelTranslations["Pillar3"] = Vector3f(-3.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 3.0f)
        keyFrames[24] = frame24
        val frame73 = KeyFrame()
        frame73.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRotations["BottomPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRotations["TopPlate"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame73.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame73.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame73.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame73.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame73.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame73.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame73.modelRotations["Pillar5"] = Quat4f(0.0f, 1.0f, 0.0f, 7.54979E-8f)
        frame73.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["BottomPlate"] = Vector3f(0.0f, -12.0f, 0.0f)
        frame73.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["TopPlate"] = Vector3f(0.0f, 12.0f, 0.0f)
        frame73.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame73.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[73] = frame73
    }
}