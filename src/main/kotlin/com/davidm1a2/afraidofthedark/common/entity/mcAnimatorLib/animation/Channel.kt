package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

/**
 * Channel was provided by the MC animator library and updated by myself
 *
 * @property name The name of the channel
 * @property fps The speed of the whole channel (frames per second)
 * @property totalFrames Number of the frames of this channel
 * @property mode How this animation should behave
 * @property keyFrames KeyFrames. Key is the position of that keyFrame in the frames list
 */
abstract class Channel(
    var name: String,
    val fps: Float = 0f,
    val totalFrames: Int = 0,
    val mode: ChannelMode = ChannelMode.LINEAR
) {
    protected val keyFrames: BiMap<Int, KeyFrame> = HashBiMap.create()

    /**
     * Return the previous rotation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is returned.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return The key frame
     */
    open fun getPreviousRotationKeyFrameForBox(boxName: String, currentFrame: Float): KeyFrame? {
        var previousRotationKeyFrame: KeyFrame? = null
        var previousRotationKeyFrameIndex: Int = -1

        for ((index, keyFrame) in keyFrames) {
            if (index > previousRotationKeyFrameIndex) {
                if (index <= currentFrame && keyFrame.useBoxInRotation(boxName)) {
                    previousRotationKeyFrame = keyFrame
                    previousRotationKeyFrameIndex = index
                }
            }
        }

        return previousRotationKeyFrame
    }

    /**
     * Return the next rotation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is NOT considered.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return The key frame
     */
    open fun getNextRotationKeyFrameForBox(boxName: String, currentFrame: Float): KeyFrame? {
        var nextRotationKeyFrame: KeyFrame? = null
        var nextRotationKeyFrameIndex: Int = Int.MAX_VALUE

        for ((index, keyFrame) in keyFrames) {
            if (index < nextRotationKeyFrameIndex) {
                if (index > currentFrame && keyFrame.useBoxInRotation(boxName)) {
                    nextRotationKeyFrame = keyFrame
                    nextRotationKeyFrameIndex = index
                }
            }
        }

        return nextRotationKeyFrame
    }

    /**
     * Return the previous translation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is returned.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return The key frame
     */
    open fun getPreviousTranslationKeyFrameForBox(boxName: String, currentFrame: Float): KeyFrame? {
        var previousTranslationKeyFrame: KeyFrame? = null
        var previousTranslationKeyFrameIndex: Int = -1

        for ((index, keyFrame) in keyFrames) {
            if (index > previousTranslationKeyFrameIndex) {
                if (index <= currentFrame && keyFrame.useBoxInTranslation(boxName)) {
                    previousTranslationKeyFrame = keyFrame
                    previousTranslationKeyFrameIndex = index
                }
            }
        }

        return previousTranslationKeyFrame
    }

    /**
     * Return the next translation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is NOT considered.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return The key frame
     */
    open fun getNextTranslationKeyFrameForBox(boxName: String, currentFrame: Float): KeyFrame? {
        var nextTranslationKeyFrame: KeyFrame? = null
        var nextTranslationKeyFrameIndex: Int = Int.MAX_VALUE

        for ((index, keyFrame) in keyFrames) {
            if (index < nextTranslationKeyFrameIndex) {
                if (index > currentFrame && keyFrame.useBoxInTranslation(boxName)) {
                    nextTranslationKeyFrame = keyFrame
                    nextTranslationKeyFrameIndex = index
                }
            }
        }

        return nextTranslationKeyFrame
    }

    /**
     * Get the position of the keyframe in this animation, if the keyframe exists.
     *
     * @param keyFrame The key frame to test
     * @return The position of the frame
     */
    open fun getKeyFramePosition(keyFrame: KeyFrame?): Int {
        return keyFrame?.let { keyFrames.inverse()[it] } ?: -1
    }
}