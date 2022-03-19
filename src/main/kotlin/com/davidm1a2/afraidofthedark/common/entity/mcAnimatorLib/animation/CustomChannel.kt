package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer

/**
 * Class was provided by the MC animator library
 *
 * @param name The name of the custom channel
 */
abstract class CustomChannel(name: String) : Channel(name) {
    /**
     * Write the actual behaviour of this custom animation here. It will called every tick until the animation is active.
     *
     * @param parts The parts of the model to update
     * @param handler A reference to the animation handler instance
     */
    abstract fun update(parts: Map<String, MCAModelRenderer>, handler: AnimationHandler)

    /**
     * Return the previous rotation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is returned.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return null, this isn't used for custom channels
     */
    override fun getPreviousRotationKeyFrameForBox(boxName: String, currentFrame: Float): Pair<Int, KeyFrame?> {
        return 0 to null
    }

    /**
     * Return the next rotation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is NOT considered.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return null, this isn't used for custom channels
     */
    override fun getNextRotationKeyFrameForBox(boxName: String, currentFrame: Float): Pair<Int, KeyFrame?> {
        return 0 to null
    }

    /**
     * Return the previous translation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is returned.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return null, this isn't used for custom channels
     */
    override fun getPreviousTranslationKeyFrameForBox(boxName: String, currentFrame: Float): Pair<Int, KeyFrame?> {
        return 0 to null
    }

    /**
     * Return the next translation KeyFrame before this frame that uses this box, if it exists. If currentFrame is a
     * keyFrame that uses this box, it is NOT considered.
     *
     * @param boxName The box to test
     * @param currentFrame The current frame
     * @return null, this isn't used for custom channels
     */
    override fun getNextTranslationKeyFrameForBox(boxName: String, currentFrame: Float): Pair<Int, KeyFrame?> {
        return 0 to null
    }
}