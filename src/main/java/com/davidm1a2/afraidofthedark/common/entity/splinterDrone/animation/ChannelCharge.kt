package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Charge attack animation used by the splinter drone
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelCharge internal constructor(name: String, fps: Float, totalFrames: Int, mode: Byte) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame0.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame0.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame0.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame0.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[0] = frame0
        val frame49 = KeyFrame()
        frame49.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame49.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame49.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame49.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame49.modelRenderersRotations["Sphere1"] = Quaternion(1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame49.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame49.modelRenderersRotations["Sphere2"] = Quaternion(1.0f, 0.0f, 0.0f, -4.371139E-8f)
        frame49.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame49.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame49.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame49.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame49.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[49] = frame49
        val frame99 = KeyFrame()
        frame99.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame99.modelRenderersRotations["Sphere1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["Sphere2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame99.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame99.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame99.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame99.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame99.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[99] = frame99
        val frame24 = KeyFrame()
        frame24.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame24.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame24.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame24.modelRenderersRotations["Sphere1"] = Quaternion(0.70710677f, 0.0f, 0.0f, 0.70710677f)
        frame24.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRenderersRotations["Sphere2"] = Quaternion(0.70710677f, 0.0f, 0.0f, 0.70710677f)
        frame24.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame24.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame24.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame24.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[24] = frame24
        val frame74 = KeyFrame()
        frame74.modelRenderersRotations["Pillar2"] = Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)
        frame74.modelRenderersRotations["Pillar4"] = Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)
        frame74.modelRenderersRotations["Pillar3"] = Quaternion(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame74.modelRenderersRotations["Pillar7"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame74.modelRenderersRotations["Sphere1"] = Quaternion(-0.70710677f, 0.0f, 0.0f, 0.7071068f)
        frame74.modelRenderersRotations["Pillar1"] = Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame74.modelRenderersRotations["Sphere2"] = Quaternion(-0.70710677f, 0.0f, 0.0f, 0.7071068f)
        frame74.modelRenderersRotations["Pillar6"] = Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)
        frame74.modelRenderersRotations["Pillar8"] = Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)
        frame74.modelRenderersRotations["Pillar5"] = Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)
        frame74.modelRenderersTranslations["Pillar2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar4"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar3"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar7"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Sphere1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar1"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Sphere2"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar6"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar8"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame74.modelRenderersTranslations["Pillar5"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[74] = frame74
    }
}