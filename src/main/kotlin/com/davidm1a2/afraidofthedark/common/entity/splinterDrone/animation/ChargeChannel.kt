package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Charge attack animation used by the splinter drone
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChargeChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRotations["Pillar5"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame0.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[0] = frame0
        val frame49 = KeyFrame()
        frame49.modelRotations["Pillar2"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame49.modelRotations["Pillar4"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame49.modelRotations["Pillar3"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame49.modelRotations["Pillar7"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame49.modelRotations["Sphere1"] = Quat4f(1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame49.modelRotations["Pillar1"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame49.modelRotations["Sphere2"] = Quat4f(1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame49.modelRotations["Pillar6"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame49.modelRotations["Pillar8"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame49.modelRotations["Pillar5"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame49.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[49] = frame49
        val frame99 = KeyFrame()
        frame99.modelRotations["Pillar2"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRotations["Pillar4"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRotations["Pillar3"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRotations["Pillar7"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRotations["Sphere1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["Pillar1"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["Sphere2"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRotations["Pillar6"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRotations["Pillar8"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRotations["Pillar5"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame99.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[99] = frame99
        val frame24 = KeyFrame()
        frame24.modelRotations["Pillar2"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRotations["Pillar4"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRotations["Pillar3"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRotations["Pillar7"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame24.modelRotations["Sphere1"] = Quat4f(0.70710677f, 0.0f, 0.0f, 0.70710677f)
        frame24.modelRotations["Pillar1"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRotations["Sphere2"] = Quat4f(0.70710677f, 0.0f, 0.0f, 0.70710677f)
        frame24.modelRotations["Pillar6"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRotations["Pillar8"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRotations["Pillar5"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame24.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[24] = frame24
        val frame74 = KeyFrame()
        frame74.modelRotations["Pillar2"] = Quat4f(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame74.modelRotations["Pillar4"] = Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame74.modelRotations["Pillar3"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame74.modelRotations["Pillar7"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame74.modelRotations["Sphere1"] = Quat4f(-0.70710677f, 0.0f, 0.0f, 0.7071068f)
        frame74.modelRotations["Pillar1"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame74.modelRotations["Sphere2"] = Quat4f(-0.70710677f, 0.0f, 0.0f, 0.7071068f)
        frame74.modelRotations["Pillar6"] = Quat4f(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame74.modelRotations["Pillar8"] = Quat4f(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame74.modelRotations["Pillar5"] = Quat4f(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame74.modelTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[74] = frame74
    }
}