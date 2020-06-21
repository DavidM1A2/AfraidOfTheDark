package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Dance animation used by enaria in the nightmare realm
 *
 * @constructor just calls super with parameters
 * @param name        The name of the channel
 * @param fps         The FPS of the animation
 * @param totalFrames The number of frames in the animation
 * @param mode        The animation mode to use
 */
class ChannelDance(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     *
     * All code below is created by the MC animator software
     */
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["head"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["leftleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["body"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame0.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame0.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame0.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[0] = frame0
        val frame130 = KeyFrame()
        frame130.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame130.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame130.modelRotations["head"] = Quat4f(0.019156948f, -0.3781637f, -0.04682582f, 0.9243552f)
        frame130.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame130.modelRotations["leftleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame130.modelRotations["body"] = Quat4f(0.0f, -0.70090926f, 0.0f, 0.71325046f)
        frame130.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame130.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame130.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame130.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame130.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame130.modelTranslations["body"] = Vector3f(-30.0f, 2.0f, 2.0f)
        keyFrames[130] = frame130
        val frame135 = KeyFrame()
        frame135.modelRotations["head"] = Quat4f(0.0f, -0.5714299f, 0.0f, 0.8206509f)
        frame135.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[135] = frame135
        val frame170 = KeyFrame()
        frame170.modelRotations["leftarm"] = Quat4f(-0.3313379f, 0.0f, 0.0f, 0.94351214f)
        frame170.modelRotations["rightleg"] =
            Quat4f(-0.39341342f, 0.0047545787f, -0.053014386f, 0.91781956f)
        frame170.modelRotations["rightarm"] = Quat4f(0.3313379f, 0.0f, 0.0f, 0.94351214f)
        frame170.modelRotations["leftleg"] = Quat4f(0.393853f, 0.026504928f, 0.06169189f, 0.91671777f)
        frame170.modelRotations["body"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame170.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame170.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame170.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame170.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame170.modelTranslations["body"] = Vector3f(-15.0f, 12.0f, 2.0f)
        keyFrames[170] = frame170
        val frame235 = KeyFrame()
        frame235.modelRotations["leftarm"] = Quat4f(-0.089179166f, -0.023430947f, 0.4556857f, 0.88535225f)
        frame235.modelRotations["rightarm"] = Quat4f(-0.15583336f, 0.11884418f, -0.4869865f, 0.8511381f)
        frame235.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame235.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[235] = frame235
        val frame140 = KeyFrame()
        frame140.modelRotations["head"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame140.modelRotations["leftleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame140.modelRotations["body"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame140.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame140.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame140.modelTranslations["body"] = Vector3f(-30.0f, 2.0f, 2.0f)
        keyFrames[140] = frame140
        val frame204 = KeyFrame()
        frame204.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.70090926f, 0.71325046f)
        frame204.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, -0.71325046f, 0.70090926f)
        frame204.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame204.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        keyFrames[204] = frame204
        val frame110 = KeyFrame()
        frame110.modelRotations["leftarm"] = Quat4f(-0.0618741f, -0.1637141f, 0.4520731f, 0.8746424f)
        frame110.modelRotations["rightleg"] = Quat4f(-0.40992305f, 0.0f, 0.0f, 0.9121201f)
        frame110.modelRotations["head"] = Quat4f(-0.1328944f, 0.045519046f, 0.040491916f, 0.989256f)
        frame110.modelRotations["rightarm"] = Quat4f(-0.11502661f, -0.02034384f, -0.5898031f, 0.799054f)
        frame110.modelRotations["leftleg"] = Quat4f(0.34693563f, 0.0f, 0.0f, 0.9378889f)
        frame110.modelRotations["body"] = Quat4f(0.0f, -0.70090926f, 0.0f, 0.71325046f)
        frame110.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame110.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame110.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame110.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame110.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame110.modelTranslations["body"] = Vector3f(-17.0f, 12.0f, 2.0f)
        keyFrames[110] = frame110
        val frame240 = KeyFrame()
        frame240.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame240.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame240.modelRotations["body"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame240.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame240.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame240.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[240] = frame240
        val frame150 = KeyFrame()
        frame150.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame150.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame150.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame150.modelRotations["body"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame150.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame150.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame150.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame150.modelTranslations["body"] = Vector3f(-30.0f, 2.0f, 2.0f)
        keyFrames[150] = frame150
        val frame280 = KeyFrame()
        frame280.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame280.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame280.modelRotations["head"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame280.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame280.modelRotations["leftleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame280.modelRotations["body"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame280.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame280.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame280.modelTranslations["head"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame280.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame280.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame280.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[280] = frame280
        val frame90 = KeyFrame()
        frame90.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame90.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame90.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame90.modelRotations["leftleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame90.modelRotations["body"] = Quat4f(0.0f, -0.70090926f, 0.0f, 0.71325046f)
        frame90.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame90.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame90.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame90.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame90.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[90] = frame90
        val frame60 = KeyFrame()
        frame60.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame60.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame60.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame60.modelRotations["body"] = Quat4f(0.0f, 8.726645E-4f, 0.0f, 0.99999964f)
        frame60.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame60.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame60.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame60.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[60] = frame60
        val frame220 = KeyFrame()
        frame220.modelRotations["body"] = Quat4f(0.0f, -1.0f, 0.0f, -4.371139E-8f)
        frame220.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[220] = frame220
        val frame61 = KeyFrame()
        frame61.modelRotations["body"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame61.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[61] = frame61
        val frame221 = KeyFrame()
        frame221.modelRotations["leftarm"] = Quat4f(-0.0044424776f, 0.0044424776f, 0.63891906f, 0.7692483f)
        frame221.modelRotations["rightarm"] = Quat4f(-0.07202796f, 0.07084146f, -0.68537843f, 0.72114486f)
        frame221.modelRotations["body"] = Quat4f(0.0f, -0.9994209f, 0.0f, 0.034027282f)
        frame221.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame221.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame221.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[221] = frame221
        val frame30 = KeyFrame()
        frame30.modelRotations["leftarm"] = Quat4f(-0.9928517f, -0.08424469f, 0.08424469f, 0.007148222f)
        frame30.modelRotations["rightleg"] = Quat4f(0.7221783f, 0.058252096f, -0.061277717f, 0.68652034f)
        frame30.modelRotations["rightarm"] = Quat4f(-0.7582637f, 0.025816685f, -0.6510593f, 0.022166627f)
        frame30.modelRotations["body"] = Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f)
        frame30.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame30.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame30.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame30.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[30] = frame30
        val frame190 = KeyFrame()
        frame190.modelRotations["leftarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame190.modelRotations["rightleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame190.modelRotations["rightarm"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame190.modelRotations["leftleg"] = Quat4f(0.0f, 0.0f, 0.0f, 1.0f)
        frame190.modelRotations["body"] = Quat4f(0.0f, 0.70710677f, 0.0f, 0.70710677f)
        frame190.modelTranslations["leftarm"] = Vector3f(4.0f, -2.0f, 0.0f)
        frame190.modelTranslations["rightleg"] = Vector3f(-2.0f, -12.0f, 0.0f)
        frame190.modelTranslations["rightarm"] = Vector3f(-4.0f, -2.0f, 0.0f)
        frame190.modelTranslations["leftleg"] = Vector3f(2.0f, -12.0f, 0.0f)
        frame190.modelTranslations["body"] = Vector3f(0.0f, 2.0f, 2.0f)
        keyFrames[190] = frame190
    }
}