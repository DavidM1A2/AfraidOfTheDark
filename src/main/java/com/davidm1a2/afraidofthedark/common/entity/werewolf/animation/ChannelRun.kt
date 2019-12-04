package com.davidm1a2.afraidofthedark.common.entity.werewolf.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Run animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelRun internal constructor(name: String, fps: Float, totalFrames: Int, mode: Byte) : Channel(name, fps, totalFrames, mode)
{
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames()
    {
        val frame21 = KeyFrame()
        frame21.modelRenderersRotations["LeftFrontLeg"] = Quaternion(-0.5983246f, 0.0f, 0.0f, 0.8012538f)
        frame21.modelRenderersRotations["LeftBackLeg"] = Quaternion(0.3632512f, 0.0f, 0.0f, 0.9316912f)
        frame21.modelRenderersRotations["LefttFrontFoot"] = Quaternion(0.1175374f, 0.0f, 0.0f, 0.99306846f)
        frame21.modelRenderersRotations["BodyUpper"] = Quaternion(-0.043619387f, 0.0f, 0.0f, 0.99904823f)
        frame21.modelRenderersRotations["RightFrontLeg"] = Quaternion(-0.529179f, 0.0f, 0.0f, 0.8485102f)
        frame21.modelRenderersRotations["RightBackLeg"] = Quaternion(0.42498952f, 0.0f, 0.0f, 0.9051983f)
        frame21.modelRenderersRotations["RightFrontFoot"] = Quaternion(0.15126081f, 0.0f, 0.0f, 0.98849386f)
        frame21.modelRenderersRotations["LeftBackLowerLeg"] = Quaternion(0.1010563f, 0.0f, 0.0f, 0.99488074f)
        frame21.modelRenderersRotations["Head"] = Quaternion(0.1175374f, 0.0f, 0.0f, 0.99306846f)
        frame21.modelRenderersRotations["BodyLower"] = Quaternion(0.050592944f, 0.0f, 0.0f, 0.99871933f)
        frame21.modelRenderersRotations["LeftBackFoot"] = Quaternion(0.1010563f, 0.0f, 0.0f, 0.99488074f)
        frame21.modelRenderersTranslations["LeftFrontLeg"] = Vector3f(4.5f, -4.0f, 9.0f)
        frame21.modelRenderersTranslations["LeftBackLeg"] = Vector3f(3.0f, -3.5f, -11.0f)
        frame21.modelRenderersTranslations["LefttFrontFoot"] = Vector3f(0.0f, -7.0f, -0.5f)
        frame21.modelRenderersTranslations["BodyUpper"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame21.modelRenderersTranslations["RightFrontLeg"] = Vector3f(-4.5f, -4.0f, 9.0f)
        frame21.modelRenderersTranslations["RightBackLeg"] = Vector3f(-3.0f, -3.5f, -11.0f)
        frame21.modelRenderersTranslations["RightFrontFoot"] = Vector3f(0.0f, -7.0f, -0.5f)
        frame21.modelRenderersTranslations["LeftBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame21.modelRenderersTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        frame21.modelRenderersTranslations["BodyLower"] = Vector3f(0.0f, 0.0f, 0.5f)
        frame21.modelRenderersTranslations["LeftBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        keyFrames[21] = frame21
        val frame11 = KeyFrame()
        frame11.modelRenderersRotations["LeftFrontLeg"] = Quaternion(0.39474386f, 0.0f, 0.0f, 0.91879123f)
        frame11.modelRenderersRotations["LeftBackLeg"] = Quaternion(-0.45399052f, 0.0f, 0.0f, 0.8910065f)
        frame11.modelRenderersRotations["LefttFrontFoot"] = Quaternion(-0.084547415f, 0.0f, 0.0f, 0.9964194f)
        frame11.modelRenderersRotations["RightBackFoot"] = Quaternion(0.016579868f, 0.0f, 0.0f, 0.99986255f)
        frame11.modelRenderersRotations["BodyUpper"] = Quaternion(0.043619387f, 0.0f, 0.0f, 0.99904823f)
        frame11.modelRenderersRotations["RightFrontLeg"] = Quaternion(0.39474386f, 0.0f, 0.0f, 0.91879123f)
        frame11.modelRenderersRotations["RightBackLeg"] = Quaternion(-0.45554492f, 0.0f, 0.0f, 0.89021283f)
        frame11.modelRenderersRotations["RightFrontFoot"] = Quaternion(-0.084547415f, 0.0f, 0.0f, 0.9964194f)
        frame11.modelRenderersRotations["LeftBackLowerLeg"] = Quaternion(0.21643962f, 0.0f, 0.0f, 0.976296f)
        frame11.modelRenderersRotations["RightBackLowerLeg"] = Quaternion(0.21814324f, 0.0f, 0.0f, 0.97591674f)
        frame11.modelRenderersRotations["Head"] = Quaternion(0.016579868f, 0.0f, 0.0f, 0.99986255f)
        frame11.modelRenderersRotations["BodyLower"] = Quaternion(-0.06975647f, 0.0f, 0.0f, 0.9975641f)
        frame11.modelRenderersRotations["LeftBackFoot"] = Quaternion(0.016579868f, 0.0f, 0.0f, 0.99986255f)
        frame11.modelRenderersTranslations["LeftFrontLeg"] = Vector3f(4.5f, -4.0f, 9.0f)
        frame11.modelRenderersTranslations["LeftBackLeg"] = Vector3f(3.0f, -3.5f, -11.0f)
        frame11.modelRenderersTranslations["LefttFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame11.modelRenderersTranslations["RightBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        frame11.modelRenderersTranslations["BodyUpper"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame11.modelRenderersTranslations["RightFrontLeg"] = Vector3f(-4.5f, -4.0f, 9.0f)
        frame11.modelRenderersTranslations["RightBackLeg"] = Vector3f(-3.0f, -3.5f, -11.0f)
        frame11.modelRenderersTranslations["RightFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame11.modelRenderersTranslations["LeftBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame11.modelRenderersTranslations["RightBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame11.modelRenderersTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        frame11.modelRenderersTranslations["BodyLower"] = Vector3f(0.0f, 0.0f, 0.5f)
        frame11.modelRenderersTranslations["LeftBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        keyFrames[11] = frame11
        val frame31 = KeyFrame()
        frame31.modelRenderersRotations["LeftFrontLeg"] = Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f)
        frame31.modelRenderersRotations["LeftBackLeg"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRenderersRotations["LefttFrontFoot"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRenderersRotations["RightBackFoot"] = Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f)
        frame31.modelRenderersRotations["BodyUpper"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame31.modelRenderersRotations["RightFrontLeg"] = Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f)
        frame31.modelRenderersRotations["RightBackLeg"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRenderersRotations["RightFrontFoot"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRenderersRotations["LeftBackLowerLeg"] = Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f)
        frame31.modelRenderersRotations["RightBackLowerLeg"] = Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f)
        frame31.modelRenderersRotations["Head"] = Quaternion(0.0784591f, 0.0f, 0.0f, 0.9969173f)
        frame31.modelRenderersRotations["BodyLower"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame31.modelRenderersRotations["LeftBackFoot"] = Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f)
        frame31.modelRenderersTranslations["LeftFrontLeg"] = Vector3f(4.5f, -4.0f, 9.0f)
        frame31.modelRenderersTranslations["LeftBackLeg"] = Vector3f(3.0f, -3.5f, -11.0f)
        frame31.modelRenderersTranslations["LefttFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame31.modelRenderersTranslations["RightBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        frame31.modelRenderersTranslations["BodyUpper"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame31.modelRenderersTranslations["RightFrontLeg"] = Vector3f(-4.5f, -4.0f, 9.0f)
        frame31.modelRenderersTranslations["RightBackLeg"] = Vector3f(-3.0f, -3.5f, -11.0f)
        frame31.modelRenderersTranslations["RightFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame31.modelRenderersTranslations["LeftBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame31.modelRenderersTranslations["RightBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame31.modelRenderersTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        frame31.modelRenderersTranslations["BodyLower"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame31.modelRenderersTranslations["LeftBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        keyFrames[31] = frame31
    }
}