package com.davidm1a2.afraidofthedark.common.entity.werewolf.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f

/**
 * Run animation used by the enchanted skeleton
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class RunChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame21 = KeyFrame()
        frame21.modelRotations["LeftFrontLeg"] = Quaternion(-0.5983246f, 0.0f, 0.0f, 0.8012538f)
        frame21.modelRotations["LeftBackLeg"] = Quaternion(0.3632512f, 0.0f, 0.0f, 0.9316912f)
        frame21.modelRotations["LefttFrontFoot"] = Quaternion(0.1175374f, 0.0f, 0.0f, 0.99306846f)
        frame21.modelRotations["BodyUpper"] = Quaternion(-0.043619387f, 0.0f, 0.0f, 0.99904823f)
        frame21.modelRotations["RightFrontLeg"] = Quaternion(-0.529179f, 0.0f, 0.0f, 0.8485102f)
        frame21.modelRotations["RightBackLeg"] = Quaternion(0.42498952f, 0.0f, 0.0f, 0.9051983f)
        frame21.modelRotations["RightFrontFoot"] = Quaternion(0.15126081f, 0.0f, 0.0f, 0.98849386f)
        frame21.modelRotations["LeftBackLowerLeg"] = Quaternion(0.1010563f, 0.0f, 0.0f, 0.99488074f)
        frame21.modelRotations["Head"] = Quaternion(0.1175374f, 0.0f, 0.0f, 0.99306846f)
        frame21.modelRotations["BodyLower"] = Quaternion(0.050592944f, 0.0f, 0.0f, 0.99871933f)
        frame21.modelRotations["LeftBackFoot"] = Quaternion(0.1010563f, 0.0f, 0.0f, 0.99488074f)
        frame21.modelTranslations["LeftFrontLeg"] = Vector3f(4.5f, -4.0f, 9.0f)
        frame21.modelTranslations["LeftBackLeg"] = Vector3f(3.0f, -3.5f, -11.0f)
        frame21.modelTranslations["LefttFrontFoot"] = Vector3f(0.0f, -7.0f, -0.5f)
        frame21.modelTranslations["BodyUpper"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame21.modelTranslations["RightFrontLeg"] = Vector3f(-4.5f, -4.0f, 9.0f)
        frame21.modelTranslations["RightBackLeg"] = Vector3f(-3.0f, -3.5f, -11.0f)
        frame21.modelTranslations["RightFrontFoot"] = Vector3f(0.0f, -7.0f, -0.5f)
        frame21.modelTranslations["LeftBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame21.modelTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        frame21.modelTranslations["BodyLower"] = Vector3f(0.0f, 0.0f, 0.5f)
        frame21.modelTranslations["LeftBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        keyFrames[21] = frame21
        val frame11 = KeyFrame()
        frame11.modelRotations["LeftFrontLeg"] = Quaternion(0.39474386f, 0.0f, 0.0f, 0.91879123f)
        frame11.modelRotations["LeftBackLeg"] = Quaternion(-0.45399052f, 0.0f, 0.0f, 0.8910065f)
        frame11.modelRotations["LefttFrontFoot"] = Quaternion(-0.084547415f, 0.0f, 0.0f, 0.9964194f)
        frame11.modelRotations["RightBackFoot"] = Quaternion(0.016579868f, 0.0f, 0.0f, 0.99986255f)
        frame11.modelRotations["BodyUpper"] = Quaternion(0.043619387f, 0.0f, 0.0f, 0.99904823f)
        frame11.modelRotations["RightFrontLeg"] = Quaternion(0.39474386f, 0.0f, 0.0f, 0.91879123f)
        frame11.modelRotations["RightBackLeg"] = Quaternion(-0.45554492f, 0.0f, 0.0f, 0.89021283f)
        frame11.modelRotations["RightFrontFoot"] = Quaternion(-0.084547415f, 0.0f, 0.0f, 0.9964194f)
        frame11.modelRotations["LeftBackLowerLeg"] = Quaternion(0.21643962f, 0.0f, 0.0f, 0.976296f)
        frame11.modelRotations["RightBackLowerLeg"] = Quaternion(0.21814324f, 0.0f, 0.0f, 0.97591674f)
        frame11.modelRotations["Head"] = Quaternion(0.016579868f, 0.0f, 0.0f, 0.99986255f)
        frame11.modelRotations["BodyLower"] = Quaternion(-0.06975647f, 0.0f, 0.0f, 0.9975641f)
        frame11.modelRotations["LeftBackFoot"] = Quaternion(0.016579868f, 0.0f, 0.0f, 0.99986255f)
        frame11.modelTranslations["LeftFrontLeg"] = Vector3f(4.5f, -4.0f, 9.0f)
        frame11.modelTranslations["LeftBackLeg"] = Vector3f(3.0f, -3.5f, -11.0f)
        frame11.modelTranslations["LefttFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame11.modelTranslations["RightBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        frame11.modelTranslations["BodyUpper"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame11.modelTranslations["RightFrontLeg"] = Vector3f(-4.5f, -4.0f, 9.0f)
        frame11.modelTranslations["RightBackLeg"] = Vector3f(-3.0f, -3.5f, -11.0f)
        frame11.modelTranslations["RightFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame11.modelTranslations["LeftBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame11.modelTranslations["RightBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame11.modelTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        frame11.modelTranslations["BodyLower"] = Vector3f(0.0f, 0.0f, 0.5f)
        frame11.modelTranslations["LeftBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        keyFrames[11] = frame11
        val frame31 = KeyFrame()
        frame31.modelRotations["LeftFrontLeg"] = Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f)
        frame31.modelRotations["LeftBackLeg"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRotations["LefttFrontFoot"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRotations["RightBackFoot"] = Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f)
        frame31.modelRotations["BodyUpper"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame31.modelRotations["RightFrontLeg"] = Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f)
        frame31.modelRotations["RightBackLeg"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRotations["RightFrontFoot"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame31.modelRotations["LeftBackLowerLeg"] = Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f)
        frame31.modelRotations["RightBackLowerLeg"] = Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f)
        frame31.modelRotations["Head"] = Quaternion(0.0784591f, 0.0f, 0.0f, 0.9969173f)
        frame31.modelRotations["BodyLower"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame31.modelRotations["LeftBackFoot"] = Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f)
        frame31.modelTranslations["LeftFrontLeg"] = Vector3f(4.5f, -4.0f, 9.0f)
        frame31.modelTranslations["LeftBackLeg"] = Vector3f(3.0f, -3.5f, -11.0f)
        frame31.modelTranslations["LefttFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame31.modelTranslations["RightBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        frame31.modelTranslations["BodyUpper"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame31.modelTranslations["RightFrontLeg"] = Vector3f(-4.5f, -4.0f, 9.0f)
        frame31.modelTranslations["RightBackLeg"] = Vector3f(-3.0f, -3.5f, -11.0f)
        frame31.modelTranslations["RightFrontFoot"] = Vector3f(0.0f, -7.0f, 0.0f)
        frame31.modelTranslations["LeftBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame31.modelTranslations["RightBackLowerLeg"] = Vector3f(0.5f, -5.0f, 0.0f)
        frame31.modelTranslations["Head"] = Vector3f(0.0f, 3.0f, 10.0f)
        frame31.modelTranslations["BodyLower"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame31.modelTranslations["LeftBackFoot"] = Vector3f(-1.0f, -4.0f, 0.0f)
        keyFrames[31] = frame31
    }
}