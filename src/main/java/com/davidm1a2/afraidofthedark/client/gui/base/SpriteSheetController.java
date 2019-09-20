package com.davidm1a2.afraidofthedark.client.gui.base;

import net.minecraft.util.math.MathHelper;

/**
 * A controller used to draw sprite sheets. The controller tells us what frame to draw
 */
public class SpriteSheetController
{
    // The delay between sprite sheet frames in milliseconds
    private final int frameDelayInMillis;
    // The total number of frames in the animation
    private final int totalFrames;
    // The width and height of the sprite sheet image
    private final int frameWidth;
    private final int frameHeight;
    // If we should interpolate frames
    private final boolean frameInterpolate;
    // If the sprite sheet is vertically or horizontally oriented
    private final boolean isVertical;

    // The percentage towards the next frame we are
    private float percentageTowardsNextFrame = 0.0f;
    // The time the last frame was shown
    private long lastFrameTime = 0;
    // The current frame index
    private int currentFrame = 0;

    /**
     * Constructor initializes all fields
     *
     * @param frameDelayInMillis The delay between sprite sheet frames in milliseconds
     * @param totalFrames        The total number of frames in the animation
     * @param frameWidth         The width of the sprite sheet image
     * @param frameHeight        The height of the sprite sheet image
     * @param frameInterpolate   If we should interpolate frames
     * @param isVertical         If the sprite sheet is vertically or horizontally oriented
     */
    public SpriteSheetController(int frameDelayInMillis, int totalFrames, int frameWidth, int frameHeight, boolean frameInterpolate, boolean isVertical)
    {
        this.frameDelayInMillis = frameDelayInMillis;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameInterpolate = frameInterpolate;
        this.isVertical = isVertical;
    }

    /**
     * Tests to see if it's time to advance to the next frame, if so perform computation to advance to the next frame
     */
    public void performUpdate()
    {
        // If we've waited long enough it's time to advance frame
        if (System.currentTimeMillis() - lastFrameTime > frameDelayInMillis)
        {
            // The last frame update was now
            lastFrameTime = System.currentTimeMillis();
            // Advance to the next frame
            currentFrame = currentFrame + 1;

            // If our current frame is past the total number of frames, go back to the beginning
            if (currentFrame > totalFrames - 1)
            {
                currentFrame = 0;
            }
        }
        // Update the percentage we are towards the next frame
        this.percentageTowardsNextFrame = MathHelper.clamp(1 - ((float) (System.currentTimeMillis() - lastFrameTime) / frameDelayInMillis), 0.0f, 1.0f);
    }

    /**
     * @return True if we should interpolate frames, false otherwise
     */
    public boolean frameInterpolate()
    {
        return this.frameInterpolate;
    }

    /**
     * @return The percent we are towards the next frame
     */
    public float getPercentageTowardsNextFrame()
    {
        return this.percentageTowardsNextFrame;
    }

    /**
     * @return True if the sprite sheet is vertical, false if it is horizontal
     */
    public boolean spriteSheetIsVertical()
    {
        return this.isVertical;
    }

    /**
     * @return The width of each sprite sheet's frame
     */
    public int getFrameWidth()
    {
        return this.frameWidth;
    }

    /**
     * @return The height of each sprite sheet's frame
     */
    public int getFrameHeight()
    {
        return this.frameHeight;
    }

    /**
     * @return The index of the current frame
     */
    public int getCurrentFrame()
    {
        return this.currentFrame;
    }

    /**
     * @return The total number of frames in the animation
     */
    public int getTotalFrames()
    {
        return this.totalFrames;
    }
}
