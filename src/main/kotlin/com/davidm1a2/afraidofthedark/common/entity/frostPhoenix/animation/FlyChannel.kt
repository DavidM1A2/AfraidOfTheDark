package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f


class FlyChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    init {
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

        val frame36 = KeyFrame()
        frame36.modelRotations["phoenixRLowerWing"] = Quaternion(0.99242324f, -0.035734467f, 0.012456472f, 0.11689293f)
        frame36.modelRotations["phoenixRUpperWing"] = Quaternion(-0.98513263f, 0.16839181f, -0.005733204f, 0.033540867f)
        frame36.modelRotations["phoenixLUpperWing"] = Quaternion(0.9914247f, -0.11902851f, 0.027805094f, 0.046218798f)
        frame36.modelRotations["phoenixLLowerWing"] = Quaternion(0.034027338f, 2.9694467E-5f, 8.721594E-4f, 0.9994205f)
        frame36.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame36.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame36.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        frame36.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        keyFrames[36] = frame36

        val frame23 = KeyFrame()
        frame23.modelRotations["phoenixRLowerWing"] = Quaternion(0.99242324f, -0.035734467f, 0.012456472f, 0.11689293f)
        frame23.modelRotations["phoenixRUpperWing"] = Quaternion(-0.98513263f, 0.16839181f, -0.005733204f, 0.033540867f)
        frame23.modelRotations["phoenixLUpperWing"] = Quaternion(0.9914247f, -0.11902851f, 0.027805094f, 0.046218798f)
        frame23.modelRotations["phoenixLLowerWing"] = Quaternion(0.034027338f, 2.9694467E-5f, 8.721594E-4f, 0.9994205f)
        frame23.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame23.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame23.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        frame23.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        keyFrames[23] = frame23

        val frame59 = KeyFrame()
        frame59.modelRotations["phoenixRWingtip"] = Quaternion(0.06621249f, -0.0131246885f, 0.051583655f, 0.9963848f)
        frame59.modelRotations["phoenixRLowerWing"] = Quaternion(0.99249333f, 0.003999486f, -0.033791486f, 0.11746933f)
        frame59.modelRotations["phoenixRUpperWing"] = Quaternion(-0.93244106f, 0.24804477f, -0.23862757f, 0.10992801f)
        frame59.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, -0.5714299f, 0.8206509f)
        frame59.modelRotations["phoenixLWingtip"] = Quaternion(0.067144625f, 0.0f, 0.0f, 0.99774325f)
        frame59.modelRotations["phoenixLUpperWing"] = Quaternion(0.9207893f, -0.20582081f, -0.3233582f, 0.07227909f)
        frame59.modelRotations["phoenixLLowerWing"] = Quaternion(0.15124002f, -0.0025078843f, -0.016389098f, 0.988358f)
        frame59.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame59.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame59.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame59.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 7.0f, 0.0f)
        frame59.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame59.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        frame59.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        keyFrames[59] = frame59

        val frame29 = KeyFrame()
        frame29.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, -0.5714299f, 0.8206509f)
        frame29.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 8.0f, 0.0f)
        keyFrames[29] = frame29

        val frame30 = KeyFrame()
        frame30.modelRotations["phoenixRWingtip"] = Quaternion(0.07822003f, 0.18840659f, 0.053458177f, 0.97751045f)
        frame30.modelRotations["phoenixLWingtip"] = Quaternion(0.025093287f, 0.23289691f, -0.04102885f, 0.97131145f)
        frame30.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame30.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        keyFrames[30] = frame30
    }
}