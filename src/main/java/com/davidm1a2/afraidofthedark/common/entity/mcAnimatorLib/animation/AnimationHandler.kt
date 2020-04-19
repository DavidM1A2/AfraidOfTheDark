package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.setAndReturn
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.slerp
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.transposeAndReturn
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * Constructor takes in all available animation channels
 *
 * @param animChannels A list of channels this animation handler must support
 * @property availableAnimChannels A map of name to available channels
 * @property animCurrentChannels List of all the activate animations of this Entity
 * @property animPrevTime Previous time of every active animation
 * @property animCurrentFrame Current frame of every active animation
 */
abstract class AnimationHandler(animChannels: Set<Channel>) {
    private val animCurrentChannels = mutableSetOf<Channel>()
    private val animPrevTime = mutableMapOf<String, Long>()
    private val animCurrentFrame = mutableMapOf<String, Float>()
    private val availableAnimChannels = animChannels.map { it.name to it }.toMap()

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
        val side = FMLCommonHandler.instance().effectiveSide
        return if (side.isServer || side.isClient && !isGamePaused()) {
            if (channel.mode != ChannelMode.CUSTOM) {
                val prevTime = animPrevTime[channel.name]!!
                val prevFrame = animCurrentFrame[channel.name]!!
                val currentTime = System.nanoTime()
                val deltaTime = (currentTime - prevTime) / 1000000000.0
                val numberOfSkippedFrames = (deltaTime * channel.fps).toFloat()
                val currentFrame = prevFrame + numberOfSkippedFrames
                // -1 as the first frame mustn't be "executed" as it is the starting situation
                if (currentFrame < channel.totalFrames - 1) {
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
    @SideOnly(Side.CLIENT)
    private fun isGamePaused(): Boolean {
        val minecraft = Minecraft.getMinecraft()
        return minecraft.isSingleplayer && minecraft.currentScreen != null && minecraft.currentScreen!!.doesGuiPauseGame() && !minecraft.integratedServer!!.public
    }

    /**
     * Apply animations if running or apply initial values. Must be called only by the model class
     *
     * @param parts The parts of the model to update
     */
    @SideOnly(Side.CLIENT)
    fun performAnimationInModel(parts: Map<String, MCAModelRenderer>) {
        for ((boxName, box) in parts) {
            var anyRotationApplied = false
            var anyTranslationApplied = false
            var anyCustomAnimationRunning = false

            for (channel in animCurrentChannels) {
                if (channel.mode != ChannelMode.CUSTOM) {
                    val currentFrame = animCurrentFrame[channel.name]!!

                    // Rotations
                    val prevRotationKeyFrame =
                        channel.getPreviousRotationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)
                    val prevRotationKeyFramePosition =
                        prevRotationKeyFrame?.let { channel.getKeyFramePosition(it) } ?: 0
                    val nextRotationKeyFrame =
                        channel.getNextRotationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)
                    val nextRotationKeyFramePosition =
                        nextRotationKeyFrame?.let { channel.getKeyFramePosition(it) } ?: 0

                    var slerpProgress =
                        (currentFrame - prevRotationKeyFramePosition) / (nextRotationKeyFramePosition - prevRotationKeyFramePosition)
                    slerpProgress = slerpProgress.coerceIn(0f, 1f)

                    if (prevRotationKeyFramePosition == 0 && prevRotationKeyFrame == null && nextRotationKeyFramePosition != 0) {
                        val currentQuat = Quat4f()
                        currentQuat.slerp(
                            box.defaultRotationAsQuat4f!!,
                            nextRotationKeyFrame!!.modelRotations[boxName]!!,
                            slerpProgress
                        )
                        box.rotationMatrix.setAndReturn(currentQuat).transposeAndReturn()
                        anyRotationApplied = true
                    } else if (prevRotationKeyFramePosition == 0 && prevRotationKeyFrame != null && nextRotationKeyFramePosition != 0) {
                        val currentQuat = Quat4f()
                        currentQuat.slerp(
                            prevRotationKeyFrame.modelRotations[boxName]!!,
                            nextRotationKeyFrame!!.modelRotations[boxName]!!,
                            slerpProgress
                        )
                        box.rotationMatrix.setAndReturn(currentQuat).transposeAndReturn()
                        anyRotationApplied = true
                    } else if (prevRotationKeyFramePosition != 0 && nextRotationKeyFramePosition != 0) {
                        val currentQuat = Quat4f()
                        currentQuat.slerp(
                            prevRotationKeyFrame!!.modelRotations[boxName]!!,
                            nextRotationKeyFrame!!.modelRotations[boxName]!!,
                            slerpProgress
                        )
                        box.rotationMatrix.setAndReturn(currentQuat).transposeAndReturn()
                        anyRotationApplied = true
                    }

                    // Translations
                    val prevTranslationKeyFrame =
                        channel.getPreviousTranslationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)
                    val prevTranslationsKeyFramePosition =
                        prevTranslationKeyFrame?.let { channel.getKeyFramePosition(it) } ?: 0
                    val nextTranslationKeyFrame =
                        channel.getNextTranslationKeyFrameForBox(boxName, animCurrentFrame[channel.name]!!)
                    val nextTranslationsKeyFramePosition =
                        nextTranslationKeyFrame?.let { channel.getKeyFramePosition(it) } ?: 0

                    var lerpProgress =
                        (currentFrame - prevTranslationsKeyFramePosition) / (nextTranslationsKeyFramePosition - prevTranslationsKeyFramePosition)
                    lerpProgress = lerpProgress.coerceAtMost(1f)

                    if (prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame == null && nextTranslationsKeyFramePosition != 0) {
                        val startPosition = box.getRotationPointAsVector()
                        val endPosition = nextTranslationKeyFrame!!.modelTranslations[boxName]
                        val currentPosition = Vector3f(startPosition)
                        currentPosition.interpolate(endPosition, lerpProgress)
                        box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z)
                        anyTranslationApplied = true
                    } else if (prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame != null && nextTranslationsKeyFramePosition != 0) {
                        val startPosition = prevTranslationKeyFrame.modelTranslations[boxName]
                        val endPosition = nextTranslationKeyFrame!!.modelTranslations[boxName]
                        val currentPosition = Vector3f(startPosition)
                        currentPosition.interpolate(endPosition, lerpProgress)
                        box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z)
                    } else if (prevTranslationsKeyFramePosition != 0 && nextTranslationsKeyFramePosition != 0) {
                        val startPosition = prevTranslationKeyFrame!!.modelTranslations[boxName]
                        val endPosition = nextTranslationKeyFrame!!.modelTranslations[boxName]
                        val currentPosition = Vector3f(startPosition)
                        currentPosition.interpolate(endPosition, lerpProgress)
                        box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z)
                        anyTranslationApplied = true
                    }
                } else {
                    anyCustomAnimationRunning = true
                    (channel as CustomChannel).update(parts, this)
                }
            }

            // Set the initial values for each box if necessary
            if (!anyRotationApplied && !anyCustomAnimationRunning) {
                box.resetRotationMatrix()
            }
            if (!anyTranslationApplied && !anyCustomAnimationRunning) {
                box.resetRotationPoint()
            }
        }
    }
}