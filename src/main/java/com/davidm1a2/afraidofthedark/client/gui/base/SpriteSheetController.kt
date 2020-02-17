package com.davidm1a2.afraidofthedark.client.gui.base

/**
 * A controller used to draw sprite sheets. The controller tells us what frame to draw
 *
 * @constructor initializes all fields
 * @param frameDelayInMillis The delay between sprite sheet frames in milliseconds
 * @param totalFrames        The total number of frames in the animation
 * @param frameWidth         The width of the sprite sheet image
 * @param frameHeight        The height of the sprite sheet image
 * @param frameInterpolate   If we should interpolate frames
 * @param isVertical         If the sprite sheet is vertically or horizontally oriented
 * @property percentageTowardsNextFrame The percent we are towards the next frame
 * @property lastFrameTime The time the last frame was shown
 * @property currentFrame The index of the current frame
 */
class SpriteSheetController(
        private val frameDelayInMillis: Int,
        val totalFrames: Int,
        val frameWidth: Int,
        val frameHeight: Int,
        val frameInterpolate: Boolean,
        val isVertical: Boolean
)
{
    var percentageTowardsNextFrame = 0.0f
        private set
    private var lastFrameTime: Long = 0
    var currentFrame = 0
        private set

    /**
     * Tests to see if it's time to advance to the next frame, if so perform computation to advance to the next frame
     */
    fun performUpdate()
    {
        // If we've waited long enough it's time to advance frame
        if (System.currentTimeMillis() - lastFrameTime > frameDelayInMillis)
        {
            // The last frame update was now
            lastFrameTime = System.currentTimeMillis()
            // Advance to the next frame
            currentFrame = currentFrame + 1

            // If our current frame is past the total number of frames, go back to the beginning
            if (currentFrame > totalFrames - 1)
            {
                currentFrame = 0
            }
        }
        // Update the percentage we are towards the next frame
        this.percentageTowardsNextFrame = (1 - (System.currentTimeMillis() - lastFrameTime).toFloat() / frameDelayInMillis).coerceIn(0.0f, 1.0f)
    }
}
