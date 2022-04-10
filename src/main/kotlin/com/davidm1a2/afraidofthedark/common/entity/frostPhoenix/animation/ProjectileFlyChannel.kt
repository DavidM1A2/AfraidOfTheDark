package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f

class ProjectileFlyChannel internal constructor(name: String, fps: Float, totalFrames: Int, mode: ChannelMode) :
    Channel(name, fps, totalFrames, mode) {
    init {
        val frame0 = KeyFrame()
        frame0.modelRotations["Center"] = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
        frame0.modelTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[0] = frame0

        val frame20 = KeyFrame()
        frame20.modelRotations["Center"] = Quaternion(-0.008726535f, 0.0f, 0.0f, 0.9999619f)
        frame20.modelTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[20] = frame20

        val frame10 = KeyFrame()
        frame10.modelRotations["Center"] = Quaternion(0.9999619f, 0.0f, 0.0f, 0.008726561f)
        frame10.modelTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[10] = frame10

        val frame11 = KeyFrame()
        frame11.modelRotations["Center"] = Quaternion(-0.9542403f, 0.0f, 0.0f, 0.2990408f)
        frame11.modelTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[11] = frame11

        val frame30 = KeyFrame()
        frame30.modelRotations["Center"] = Quaternion(0.9999619f, 0.0f, 0.0f, 0.008726561f)
        frame30.modelTranslations["Center"] = Vector3f(0.0f, 0.0f, 0.0f)
        keyFrames[30] = frame30
    }
}