package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.interpolate
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.slerp
import net.minecraft.client.Minecraft
import net.minecraft.util.math.vector.Quaternion
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.common.thread.EffectiveSide

/**
 * Constructor takes in all available animation channels
 *
 * @param animChannels A list of channels this animation handler must support
 * @property availableAnimChannels A map of name to available channels
 * @property animCurrentChannels List of all the activate animations of this Entity
 * @property animPrevTime Previous time of every active animation
 * @property animCurrentFrame Current frame of every active animation
 */
class AnimationHandler(vararg animChannels: Channel) {
    private val animCurrentChannels = mutableSetOf<Channel>()
    private val animPrevTime = mutableMapOf<String, Long>()
    private val animCurrentFrame = mutableMapOf<String, Float>()
    private val availableAnimChannels = animChannels.associateBy { it.name }

    /**
     * Performs one "tick" of the animation
     */
    fun update() {
        // Go over every channel, update it, and remove it if it's finished
        val it = animCurrentChannels.iterator()
        while (it.hasNext()) {
            val anim = it.next()
            val animStatus = updateAnimation(anim)
            if (!animStatus) {
                animPrevTime.remove(anim.name)
                animCurrentFrame.remove(anim.name)
                it.remove()
            }
        }
    }

    /**
     * Plays a given animation from a given frame
     *
     * @param name The animation to play
     * @param startingFrame The frame to start the animation at
     */
    fun playAnimation(name: String, startingFrame: Float = 0f) {
        val channel = availableAnimChannels[name]
        if (channel != null) {
            animCurrentChannels.add(channel)
            animPrevTime[name] = System.nanoTime()
            animCurrentFrame[name] = startingFrame
        } else {
            println("The animation called $name doesn't exist!")
        }
    }

    /**
     * Stops a given animation
     *
     * @param name The animation to stop
     */
    fun stopAnimation(name: String) {
        val channel = availableAnimChannels[name]
        if (channel != null) {
            if (animCurrentChannels.contains(channel)) {
                animCurrentChannels.remove(channel)
                animPrevTime.remove(name)
                animCurrentFrame.remove(name)
            }
        } else {
            println("The animation called $name doesn't exist!")
        }
    }

    /**
     * Tests if a given animation is active
     *
     * @param name The animation to test
     * @return True if the animation is playing, false otherwise
     */
    fun isAnimationActive(name: String): Boolean {
        return animCurrentChannels.any { it.name == name }
    }

    /**
     * Update animation values
     *
     * @param channel The channel to update
     * @return false if the animation should stop, true otherwise
     */
    private fun updateAnimation(channel: Channel): Boolean {
        val side = EffectiveSide.get()
        return if (side == LogicalSide.SERVER || side == LogicalSide.CLIENT && !isGamePaused()) {
            if (channel.mode != ChannelMode.CUSTOM) {
                val prevTime = animPrevTime[channel.name]!!
                val prevFrame = animCurrentFrame[channel.name]!!
                val currentTime = System.nanoTime()
                val deltaTime = (currentTime - prevTime) / 1000000000.0
                val numberOfSkippedFrames = (deltaTime * channel.fps).toFloat()
                val currentFrame = prevFrame + numberOfSkippedFrames
                if (currentFrame < channel.totalFrames) {
                    animPrevTime[channel.name] = currentTime
                    animCurrentFrame[channel.name] = currentFrame
                    true
                } else {
                    if (channel.mode == ChannelMode.LOOP) {
                        animPrevTime[channel.name] = currentTime
                        animCurrentFrame[channel.name] = 0f
                        return true
                    }
                    false
                }
            } else {
                true
            }
        } else {
            val currentTime = System.nanoTime()
            animPrevTime[channel.name] = currentTime
            true
        }
    }

    /**
     * @return true if the game is paused, false if not
     */
    private fun isGamePaused(): Boolean {
        val minecraft = Minecraft.getInstance()
        return minecraft.hasSingleplayerServer() && minecraft.screen != null && minecraft.screen!!.isPauseScreen && !minecraft.singleplayerServer!!.isSingleplayer
    }

    /**
     * Apply animations if running or apply initial values. Must be called only by the model class
     *
     * @param parts The parts of the model to update
     */
    fun performAnimationInModel(parts: Map<String, MCAModelRenderer>) {
        for ((boxName, box) in parts) {
            for (channel in animCurrentChannels) {
                if (channel.mode != ChannelMode.CUSTOM) {
                    val currentFrame = animCurrentFrame[channel.name]!!

                    // Rotations
                    val (prevRotationKeyFramePosition, prevRotationKeyFrame) =
                        channel.getPreviousRotationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)
                    val (nextRotationKeyFramePosition, nextRotationKeyFrame) =
                        channel.getNextRotationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)

                    var slerpProgress =
                        (currentFrame - prevRotationKeyFramePosition) / (nextRotationKeyFramePosition - prevRotationKeyFramePosition)
                    slerpProgress = slerpProgress.coerceIn(0f, 1f)

                    if (prevRotationKeyFrame == null && nextRotationKeyFrame != null) { // Before the first key frame
                        val currentQuat = Quaternion(0f, 0f, 0f, 0f)
                        currentQuat.slerp(
                            box.defaultRotation,
                            nextRotationKeyFrame.modelRotations[boxName]!!,
                            slerpProgress
                        )
                        box.rotation.set(currentQuat.i(), currentQuat.j(), currentQuat.k(), currentQuat.r())
                    } else if (prevRotationKeyFrame != null && nextRotationKeyFrame == null) { // After the last keyframe
                        val currentQuat = prevRotationKeyFrame.modelRotations[boxName]!!
                        box.rotation.set(currentQuat.i(), currentQuat.j(), currentQuat.k(), currentQuat.r())
                    } else if (prevRotationKeyFrame != null && nextRotationKeyFrame != null) { // Between keyframes
                        val currentQuat = Quaternion(0f, 0f, 0f, 0f)
                        currentQuat.slerp(
                            prevRotationKeyFrame.modelRotations[boxName]!!,
                            nextRotationKeyFrame.modelRotations[boxName]!!,
                            slerpProgress
                        )
                        box.rotation.set(currentQuat.i(), currentQuat.j(), currentQuat.k(), currentQuat.r())
                    } else { // There are no key frames
                        box.resetRotationQuaternion()
                    }

                    // Translations
                    val (prevTranslationsKeyFramePosition, prevTranslationKeyFrame) =
                        channel.getPreviousTranslationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)
                    val (nextTranslationsKeyFramePosition, nextTranslationKeyFrame) =
                        channel.getNextTranslationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)

                    var lerpProgress =
                        (currentFrame - prevTranslationsKeyFramePosition) / (nextTranslationsKeyFramePosition - prevTranslationsKeyFramePosition)
                    lerpProgress = lerpProgress.coerceAtMost(1f)

                    if (prevTranslationKeyFrame == null && nextTranslationKeyFrame != null) { // Before the first key frame
                        val startPosition = box.getRotationPointAsVector()
                        val endPosition = nextTranslationKeyFrame.modelTranslations[boxName]!!
                        val currentPosition = startPosition.copy()
                        currentPosition.interpolate(endPosition, lerpProgress)
                        box.setRotationPoint(currentPosition.x(), currentPosition.y(), currentPosition.z())
                    } else if (prevTranslationKeyFrame != null && nextTranslationKeyFrame == null) { // After the last keyframe
                        val currentPosition = prevTranslationKeyFrame.modelTranslations[boxName]!!
                        box.setRotationPoint(currentPosition.x(), currentPosition.y(), currentPosition.z())
                    } else if (prevTranslationKeyFrame != null && nextTranslationKeyFrame != null) { // Between keyframes
                        val startPosition = prevTranslationKeyFrame.modelTranslations[boxName]!!
                        val endPosition = nextTranslationKeyFrame.modelTranslations[boxName]!!
                        val currentPosition = startPosition.copy()
                        currentPosition.interpolate(endPosition, lerpProgress)
                        box.setRotationPoint(currentPosition.x(), currentPosition.y(), currentPosition.z())
                    } else { // There are no key frames
                        box.resetRotationPoint()
                    }
                } else {
                    (channel as CustomChannel).update(parts, this)
                }
            }
        }
    }
}