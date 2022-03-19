package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f


class IdleChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    init {
        val frame0 = KeyFrame()
        frame0.modelRotations["phoenixLUpperWing"] = Quaternion(0.49999997f, -0.68301266f, 0.1830127f, 0.49999994f)
        frame0.modelRotations["phoenixLLowerWing"] = Quaternion(0.976296f, 0.0f, 0.0f, 0.21643965f)
        frame0.modelRotations["phoenixLWingtip"] = Quaternion(-0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame0.modelRotations["phoenixRWingtip"] = Quaternion(0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame0.modelRotations["phoenixRLowerWing"] = Quaternion(0.21643962f, 0.0f, 0.0f, 0.976296f)
        frame0.modelRotations["phoenixRUpperWing"] = Quaternion(-0.49999997f, 0.68301266f, 0.1830127f, 0.49999994f)
        frame0.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame0.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        frame0.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame0.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame0.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame0.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        keyFrames[0] = frame0

        val frame20 = KeyFrame()
        frame20.modelRotations["phoenixLUpperWing"] = Quaternion(0.49999997f, -0.68301266f, 0.1830127f, 0.49999994f)
        frame20.modelRotations["phoenixLLowerWing"] = Quaternion(0.976296f, 0.0f, 0.0f, 0.21643965f)
        frame20.modelRotations["phoenixLWingtip"] = Quaternion(-0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame20.modelRotations["phoenixRWingtip"] = Quaternion(0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame20.modelRotations["phoenixRLowerWing"] = Quaternion(0.21643962f, 0.0f, 0.0f, 0.976296f)
        frame20.modelRotations["phoenixRUpperWing"] = Quaternion(-0.49999997f, 0.68301266f, 0.1830127f, 0.49999994f)
        frame20.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame20.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        frame20.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame20.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame20.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame20.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        keyFrames[20] = frame20

        val frame5 = KeyFrame()
        frame5.modelRotations["phoenixLUpperWing"] = Quaternion(0.6123724f, -0.70710677f, 0.0f, 0.35355335f)
        frame5.modelRotations["phoenixRUpperWing"] = Quaternion(-0.6123724f, 0.70710677f, 0.0f, 0.35355335f)
        frame5.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame5.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        keyFrames[5] = frame5

        val frame10 = KeyFrame()
        frame10.modelRotations["phoenixLUpperWing"] = Quaternion(0.49999997f, -0.68301266f, 0.1830127f, 0.49999994f)
        frame10.modelRotations["phoenixRUpperWing"] = Quaternion(-0.49999997f, 0.68301266f, 0.1830127f, 0.49999994f)
        frame10.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame10.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        keyFrames[10] = frame10

        val frame59 = KeyFrame()
        frame59.modelRotations["phoenixLUpperWing"] = Quaternion(0.49999997f, -0.68301266f, 0.1830127f, 0.49999994f)
        frame59.modelRotations["phoenixLLowerWing"] = Quaternion(0.976296f, 0.0f, 0.0f, 0.21643965f)
        frame59.modelRotations["phoenixLWingtip"] = Quaternion(-0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame59.modelRotations["phoenixRWingtip"] = Quaternion(0.9063078f, 0.0f, 0.0f, 0.42261824f)
        frame59.modelRotations["phoenixRLowerWing"] = Quaternion(0.21643962f, 0.0f, 0.0f, 0.976296f)
        frame59.modelRotations["phoenixRUpperWing"] = Quaternion(-0.49999997f, 0.68301266f, 0.1830127f, 0.49999994f)
        frame59.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame59.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        frame59.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame59.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame59.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame59.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        keyFrames[59] = frame59

        val frame15 = KeyFrame()
        frame15.modelRotations["phoenixLUpperWing"] = Quaternion(0.6123724f, -0.70710677f, 0.0f, 0.35355335f)
        frame15.modelRotations["phoenixRUpperWing"] = Quaternion(-0.6123724f, 0.70710677f, 0.0f, 0.35355335f)
        frame15.modelTranslations["phoenixLUpperWing"] = Vector3f(0.0f, 11.0f, -4.0f)
        frame15.modelTranslations["phoenixRUpperWing"] = Vector3f(0.0f, 11.0f, 4.0f)
        keyFrames[15] = frame15
    }
}