package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f


class IdleLookChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    init {
        val frame0 = KeyFrame()
        frame0.modelRotations["phoenixLUpperWing"] = Quaternion(0.49999997f, -0.68301266f, 0.1830127f, 0.49999994f)
        frame0.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame0.modelRotations["phoenixLLowerWing"] = Quaternion(0.976296f, 0.0f, 0.0f, 0.21643965f)
        frame0.modelRotations["phoenixLWingtip"] = Quaternion(-0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame0.modelRotations["phoenixRWingtip"] = Quaternion(0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame0.modelRotations["phoenixRLowerWing"] = Quaternion(0.21643962f, 0.0f, 0.0f, 0.976296f)
        frame0.modelRotations["phoenixRUpperWing"] = Quaternion(-0.49999997f, 0.68301266f, 0.1830127f, 0.49999994f)
        frame0.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame0.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame0.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f)
        frame0.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame0.modelTranslations["phoenixNeck2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        frame0.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame0.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame0.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame0.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        frame0.modelTranslations["phoenixNeck3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame0.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        frame0.modelTranslations["phoenixNeck1"] = Vector3f(0.0f, 13.0f, 0.0f)
        keyFrames[0] = frame0

        val frame35 = KeyFrame()
        frame35.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.1305262f, 0.9914449f)
        frame35.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.17364818f, 0.9848077f)
        frame35.modelRotations["phoenixHead1"] = Quaternion(0.014918708f, -0.3416946f, -0.040988814f, 0.93879825f)
        frame35.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.008726535f, 0.9999619f)
        frame35.modelTranslations["phoenixNeck2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame35.modelTranslations["phoenixNeck3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame35.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        frame35.modelTranslations["phoenixNeck1"] = Vector3f(0.0f, 13.0f, 0.0f)
        keyFrames[35] = frame35

        val frame20 = KeyFrame()
        frame20.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame20.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        keyFrames[20] = frame20

        val frame5 = KeyFrame()
        frame5.modelRotations["phoenixHead1"] = Quaternion(-0.20088655f, 0.008770896f, -0.042728473f, 0.97864294f)
        frame5.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        keyFrames[5] = frame5

        val frame40 = KeyFrame()
        frame40.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame40.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame40.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame40.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f)
        frame40.modelTranslations["phoenixNeck2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame40.modelTranslations["phoenixNeck3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame40.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        frame40.modelTranslations["phoenixNeck1"] = Vector3f(0.0f, 13.0f, 0.0f)
        keyFrames[40] = frame40

        val frame25 = KeyFrame()
        frame25.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame25.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame25.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame25.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f)
        frame25.modelTranslations["phoenixNeck2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame25.modelTranslations["phoenixNeck3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame25.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        frame25.modelTranslations["phoenixNeck1"] = Vector3f(0.0f, 13.0f, 0.0f)
        keyFrames[25] = frame25

        val frame30 = KeyFrame()
        frame30.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.1305262f, 0.9914449f)
        frame30.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.17364818f, 0.9848077f)
        frame30.modelRotations["phoenixHead1"] = Quaternion(0.014918708f, -0.3416946f, -0.040988814f, 0.93879825f)
        frame30.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.008726535f, 0.9999619f)
        frame30.modelTranslations["phoenixNeck2"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame30.modelTranslations["phoenixNeck3"] = Vector3f(0.0f, 5.0f, 0.0f)
        frame30.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        frame30.modelTranslations["phoenixNeck1"] = Vector3f(0.0f, 13.0f, 0.0f)
        keyFrames[30] = frame30

        val frame15 = KeyFrame()
        frame15.modelRotations["phoenixHead1"] = Quaternion(-0.20088655f, 0.008770896f, -0.042728473f, 0.97864294f)
        frame15.modelTranslations["phoenixHead1"] = Vector3f(0.0f, 8.0f, 0.0f)
        keyFrames[15] = frame15
    }
}