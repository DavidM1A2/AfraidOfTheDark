package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f


class AttackChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    init {
        val frame0 = KeyFrame()
        frame0.modelRotations["phoenixRWingtip"] = Quaternion(0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame0.modelRotations["phoenixRLeg"] = Quaternion(0.0f, 0.0f, 0.25881904f, 0.9659258f)
        frame0.modelRotations["phoenixLWingtip"] = Quaternion(-0.38268346f, 0.0f, 0.0f, 0.9238795f)
        frame0.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["phoenixLowerBeak"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["phoenixRLowerWing"] = Quaternion(0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame0.modelRotations["phoenixLUpperWing"] = Quaternion(0.96794367f, -0.12743223f, -0.21458796f, 0.028250998f)
        frame0.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, -0.6427876f, 0.76604444f)
        frame0.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, 0.5f, 0.8660254f)
        frame0.modelRotations["phoenixLLowerWing"] = Quaternion(0.42261827f, 0.0f, 0.0f, 0.90630776f)
        frame0.modelRotations["phoenixRUpperWing"] = Quaternion(-0.96794367f, 0.12743223f, -0.21458796f, 0.028250998f)
        frame0.modelRotations["phoenixLLeg"] = Quaternion(0.0f, 0.0f, 0.25881904f, 0.9659258f)
        frame0.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["phoenixTail"] = Quaternion(0.0f, 0.0f, -0.6087614f, 0.7933533f)
        frame0.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame0.modelTranslations["phoenixRLeg"] = Vector3f(-2.0f, -6.0f, 3.5f)
        frame0.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame0.modelTranslations["phoenixNeck1"] = Vector3f(0.0f, 13.0f, 0.0f)
        frame0.modelTranslations["phoenixLowerBeak"] = Vector3f(4.5f, -0.5f, 0.0f)
        frame0.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame0.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame0.modelTranslations["phoenixBreast"] = Vector3f(-5.0f, 0.0f, 0.0f)
        frame0.modelTranslations["phoenixNeck3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        frame0.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        frame0.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        frame0.modelTranslations["phoenixLLeg"] = Vector3f(-2.0f, -6.0f, -3.5f)
        frame0.modelTranslations["phoenixNeck2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["phoenixTail"] = Vector3f(-3.0f, -7.5f, 0.0f)
        keyFrames[0] = frame0

        val frame5 = KeyFrame()
        frame5.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, -0.57357645f, 0.81915206f)
        frame5.modelRotations["phoenixLowerBeak"] = Quaternion(0.0f, 0.0f, -0.38268346f, 0.9238795f)
        frame5.modelRotations["phoenixRUpperWing"] = Quaternion(-0.9914449f, 0.1305262f, 5.7054814E-9f, -4.3337433E-8f)
        frame5.modelRotations["phoenixTail"] = Quaternion(0.0f, 0.0f, -0.8870109f, 0.4617486f)
        frame5.modelRotations["phoenixLUpperWing"] = Quaternion(0.9914449f, -0.1305262f, 5.7054814E-9f, -4.3337433E-8f)
        frame5.modelTranslations["phoenixBreast"] = Vector3f(-5.0f, 0.0f, 0.0f)
        frame5.modelTranslations["phoenixLowerBeak"] = Vector3f(4.5f, -0.5f, 0.0f)
        frame5.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        frame5.modelTranslations["phoenixTail"] = Vector3f(-3.0f, -7.5f, 0.0f)
        frame5.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        keyFrames[5] = frame5

        val frame10 = KeyFrame()
        frame10.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, -0.6427876f, 0.76604444f)
        frame10.modelRotations["phoenixLowerBeak"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame10.modelRotations["phoenixRUpperWing"] = Quaternion(-0.96794367f, 0.12743223f, -0.21458796f, 0.028250998f)
        frame10.modelRotations["phoenixTail"] = Quaternion(0.0f, 0.0f, -0.6087614f, 0.7933533f)
        frame10.modelRotations["phoenixLUpperWing"] = Quaternion(0.96794367f, -0.12743223f, -0.21458796f, 0.028250998f)
        frame10.modelTranslations["phoenixBreast"] = Vector3f(-5.0f, 0.0f, 0.0f)
        frame10.modelTranslations["phoenixLowerBeak"] = Vector3f(4.5f, -0.5f, 0.0f)
        frame10.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        frame10.modelTranslations["phoenixTail"] = Vector3f(-3.0f, -7.5f, 0.0f)
        frame10.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        keyFrames[10] = frame10
    }
}