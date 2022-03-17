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
        frame0.modelRotations["phoenixRWingtip"] = Quaternion(0.83871186f, 0.019565567f, 0.03239297f, 0.54325897f)
        frame0.modelRotations["phoenixRLowerWing"] = Quaternion(0.19949791f, 0.02541694f, -0.045272544f, 0.9785218f)
        frame0.modelRotations["phoenixRUpperWing"] = Quaternion(-0.3791906f, 0.65555114f, 0.21367073f, 0.61709976f)
        frame0.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, 0.034899496f, 0.99939084f)
        frame0.modelRotations["phoenixLWingtip"] = Quaternion(-0.88253754f, 0.0f, 0.0f, 0.4702419f)
        frame0.modelRotations["phoenixLUpperWing"] = Quaternion(0.43009f, -0.60981834f, 0.2648184f, 0.61074984f)
        frame0.modelRotations["phoenixLLowerWing"] = Quaternion(0.96363044f, 0.0f, 0.0f, 0.26723835f)
        frame0.modelTranslations["phoenixRWingtip"] = Vector3f(0.0f, -1.0f, 11.5f)
        frame0.modelTranslations["phoenixRLowerWing"] = Vector3f(0.0f, -1.0f, -7.5f)
        frame0.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame0.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 0.0f, 0.0f)
        frame0.modelTranslations["phoenixLWingtip"] = Vector3f(0.0f, 1.0f, 11.5f)
        frame0.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        frame0.modelTranslations["phoenixLLowerWing"] = Vector3f(0.0f, -1.0f, 7.5f)
        keyFrames[0] = frame0

        val frame96 = KeyFrame()
        frame96.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame96.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame96.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f)
        frame96.modelTranslations["phoenixNeck3"] = Vector3f(1.0f, 4.0f, 1.0f)
        frame96.modelTranslations["phoenixNeck2"] = Vector3f(1.0f, 5.0f, 1.0f)
        frame96.modelTranslations["phoenixNeck1"] = Vector3f(1.0f, 12.0f, 1.0f)
        keyFrames[96] = frame96

        val frame69 = KeyFrame()
        frame69.modelRotations["phoenixHead1"] = Quaternion(0.010219754f, -0.23407084f, -0.042405277f, 0.9712405f)
        frame69.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        keyFrames[69] = frame69

        val frame103 = KeyFrame()
        frame103.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.21814324f, 0.97591674f)
        frame103.modelRotations["phoenixNeck2"] = Quaternion(4.0891967E-4f, -0.0016967485f, -0.23429348f, 0.97216433f)
        frame103.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, -0.016579868f, 0.99986255f)
        frame103.modelTranslations["phoenixNeck3"] = Vector3f(1.0f, 4.0f, 1.0f)
        frame103.modelTranslations["phoenixNeck2"] = Vector3f(1.0f, 5.0f, 1.0f)
        frame103.modelTranslations["phoenixNeck1"] = Vector3f(1.0f, 12.0f, 1.0f)
        keyFrames[103] = frame103

        val frame73 = KeyFrame()
        frame73.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame73.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        keyFrames[73] = frame73

        val frame10 = KeyFrame()
        frame10.modelRotations["phoenixLowerBeak"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame10.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame10.modelTranslations["phoenixLowerBeak"] = Vector3f(5.5f, 0.5f, 1.0f)
        frame10.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        keyFrames[10] = frame10

        val frame107 = KeyFrame()
        frame107.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.21814324f, 0.97591674f)
        frame107.modelRotations["phoenixNeck2"] = Quaternion(4.0891967E-4f, -0.0016967485f, -0.23429348f, 0.97216433f)
        frame107.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, -0.016579868f, 0.99986255f)
        frame107.modelTranslations["phoenixNeck3"] = Vector3f(1.0f, 4.0f, 1.0f)
        frame107.modelTranslations["phoenixNeck2"] = Vector3f(1.0f, 5.0f, 1.0f)
        frame107.modelTranslations["phoenixNeck1"] = Vector3f(1.0f, 12.0f, 1.0f)
        keyFrames[107] = frame107

        val frame239 = KeyFrame()
        frame239.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, 0.034899496f, 0.99939084f)
        frame239.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[239] = frame239

        val frame20 = KeyFrame()
        frame20.modelRotations["phoenixLowerBeak"] = Quaternion(0.0f, 0.0f, -0.25038f, 0.96814764f)
        frame20.modelRotations["phoenixRUpperWing"] = Quaternion(-0.3791906f, 0.65555114f, 0.21367073f, 0.61709976f)
        frame20.modelRotations["phoenixHead1"] = Quaternion(-0.007874134f, 0.18034732f, -0.042902786f, 0.9826353f)
        frame20.modelRotations["phoenixLUpperWing"] = Quaternion(0.43009f, -0.60981834f, 0.2648184f, 0.61074984f)
        frame20.modelTranslations["phoenixLowerBeak"] = Vector3f(5.5f, 0.5f, 1.0f)
        frame20.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame20.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        frame20.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        keyFrames[20] = frame20

        val frame53 = KeyFrame()
        frame53.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame53.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        keyFrames[53] = frame53

        val frame119 = KeyFrame()
        frame119.modelRotations["phoenixNeck3"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame119.modelRotations["phoenixNeck2"] = Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f)
        frame119.modelRotations["phoenixNeck1"] = Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f)
        frame119.modelRotations["phoenixBreast"] = Quaternion(0.0f, 0.0f, 0.034899496f, 0.99939084f)
        frame119.modelTranslations["phoenixNeck3"] = Vector3f(1.0f, 4.0f, 1.0f)
        frame119.modelTranslations["phoenixNeck2"] = Vector3f(1.0f, 5.0f, 1.0f)
        frame119.modelTranslations["phoenixNeck1"] = Vector3f(1.0f, 12.0f, 1.0f)
        frame119.modelTranslations["phoenixBreast"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[119] = frame119

        val frame25 = KeyFrame()
        frame25.modelRotations["phoenixRUpperWing"] = Quaternion(-0.502403f, 0.761353f, 0.11177485f, 0.39425775f)
        frame25.modelRotations["phoenixLUpperWing"] = Quaternion(0.55851895f, -0.6385469f, 0.16957921f, 0.50155497f)
        frame25.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame25.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        keyFrames[25] = frame25

        val frame57 = KeyFrame()
        frame57.modelRotations["phoenixHead1"] = Quaternion(0.010219754f, -0.23407084f, -0.042405277f, 0.9712405f)
        frame57.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        keyFrames[57] = frame57

        val frame30 = KeyFrame()
        frame30.modelRotations["phoenixLowerBeak"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame30.modelRotations["phoenixRUpperWing"] = Quaternion(-0.3791906f, 0.65555114f, 0.21367073f, 0.61709976f)
        frame30.modelRotations["phoenixHead1"] = Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f)
        frame30.modelRotations["phoenixLUpperWing"] = Quaternion(0.43009f, -0.60981834f, 0.2648184f, 0.61074984f)
        frame30.modelTranslations["phoenixLowerBeak"] = Vector3f(5.5f, 0.5f, 1.0f)
        frame30.modelTranslations["phoenixRUpperWing"] = Vector3f(2.0f, 11.0f, 8.0f)
        frame30.modelTranslations["phoenixHead1"] = Vector3f(-0.5f, 7.0f, -1.0f)
        frame30.modelTranslations["phoenixLUpperWing"] = Vector3f(2.0f, 11.0f, 0.0f)
        keyFrames[30] = frame30
    }
}