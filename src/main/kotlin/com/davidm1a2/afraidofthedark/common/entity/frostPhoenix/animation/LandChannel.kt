package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f


class LandChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    override fun initializeAllFrames() {
        val frame0 = KeyFrame()
        frame0.modelRotations["phoenixTail"] = Quaternion(0.0f, 0.0f, -0.65143657f, 0.7587031f)
        frame0.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, -0.5714299f, 0.8206509f)
        frame0.modelRotations["phoenixRUpperWing"] = Quaternion(-0.93244106f, 0.24804477f, -0.23862757f, 0.10992801f)
        frame0.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, 0.5f, 0.8660254f)
        frame0.modelRotations["phoenixLLeg"] = Quaternion(0.0f, 0.0f, 0.7693995f, 0.63876784f)
        frame0.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["phoenixRLeg"] = Quaternion(0.002679859f, 0.0022367393f, 0.76772f, 0.64077604f)
        frame0.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelRotations["phoenixRWingtip"] = Quaternion(0.06621249f, -0.0131246885f, 0.051583655f, 0.9963848f)
        frame0.modelRotations["phoenixRLowerWing"] = Quaternion(0.99249333f, 0.003999486f, -0.033791486f, 0.11746933f)
        frame0.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.002617991f, 0.9999966f)
        frame0.modelRotations["phoenixLWingtip"] = Quaternion(0.067144625f, 0.0f, 0.0f, 0.99774325f)
        frame0.modelRotations["phoenixLUpperWing"] = Quaternion(0.9207893f, -0.20582081f, -0.3233582f, 0.07227909f)
        frame0.modelRotations["phoenixLLowerWing"] = Quaternion(0.15124002f, -0.0025078843f, -0.016389098f, 0.988358f)
        frame0.modelTranslations["phoenixTail"] = Vector3f(0.0f, 0.5f, 1.5f)
        frame0.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 7.0f, 0.0f)
        frame0.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame0.modelTranslations["phoenixHead1"] = Vector3f(1.5f, 7.0f, -1.0f)
        frame0.modelTranslations["phoenixLLeg"] = Vector3f(8.0f, 0.0f, -1.5f)
        frame0.modelTranslations["phoenixNeck3"] = Vector3f(1.0f, 4.0f, 1.0f)
        frame0.modelTranslations["phoenixRLeg"] = Vector3f(8.0f, 0.0f, 5.5f)
        frame0.modelTranslations["phoenixNeck2"] = Vector3f(1.0f, 5.0f, 1.0f)
        frame0.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame0.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame0.modelTranslations["phoenixNeck1"] = Vector3f(1.0f, 12.0f, 1.0f)
        frame0.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame0.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        frame0.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        keyFrames[0] = frame0

        val frame29 = KeyFrame()
        frame29.modelRotations["phoenixTail"] = Quaternion(0.0f, 0.0f, -1.0f, -4.371139E-8f)
        frame29.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, 0.034899496f, 0.99939084f)
        frame29.modelRotations["phoenixRUpperWing"] = Quaternion(-0.5529556f, 0.60653466f, 0.46669966f, 0.32946497f)
        frame29.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame29.modelRotations["phoenixLLeg"] = Quaternion(0.0f, 0.0f, 0.4226183f, 0.90630776f)
        frame29.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame29.modelRotations["phoenixRLeg"] = Quaternion(0.0f, 0.0f, 0.42261827f, 0.90630776f)
        frame29.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame29.modelRotations["phoenixRWingtip"] = Quaternion(0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame29.modelRotations["phoenixRLowerWing"] = Quaternion(0.7372773f, 0.0f, 0.0f, 0.6755902f)
        frame29.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f)
        frame29.modelRotations["phoenixLWingtip"] = Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f)
        frame29.modelRotations["phoenixLUpperWing"] = Quaternion(0.57427645f, -0.6238206f, 0.43900543f, 0.29720137f)
        frame29.modelRotations["phoenixLLowerWing"] = Quaternion(0.67559016f, 0.0f, 0.0f, 0.7372773f)
        frame29.modelTranslations["phoenixTail"] = Vector3f(0.0f, 0.5f, 1.5f)
        frame29.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame29.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame29.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        frame29.modelTranslations["phoenixLLeg"] = Vector3f(7.0f, -4.0f, -1.5f)
        frame29.modelTranslations["phoenixNeck3"] = Vector3f(1.0f, 4.0f, 1.0f)
        frame29.modelTranslations["phoenixRLeg"] = Vector3f(7.0f, -4.0f, 5.5f)
        frame29.modelTranslations["phoenixNeck2"] = Vector3f(1.0f, 5.0f, 1.0f)
        frame29.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame29.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame29.modelTranslations["phoenixNeck1"] = Vector3f(1.0f, 12.0f, 1.0f)
        frame29.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame29.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        frame29.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        keyFrames[29] = frame29
    }
}