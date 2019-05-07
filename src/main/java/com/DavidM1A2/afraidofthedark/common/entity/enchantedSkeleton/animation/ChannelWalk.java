/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.animation;


import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

/**
 * Walk animation used by the enchanted skeleton
 */
public class ChannelWalk extends Channel
{
    /**
     * Constructor just calls super with parameters
     *
     * @param name        The name of the channel
     * @param fps         The FPS of the animation
     * @param totalFrames The number of frames in the animation
     * @param mode        The animation mode to use
     */
    ChannelWalk(String name, float fps, int totalFrames, byte mode)
    {
        super(name, fps, totalFrames, mode);
    }

    /**
     * Initializes a map of frame -> position which will be interpolated by the rendering handler
     * <p>
     * All code below is created by the MC animator software
     */
    @Override
    protected void initializeAllFrames()
    {
        KeyFrame frame0 = new KeyFrame();
        frame0.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
        frame0.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
        frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(0, frame0);

        KeyFrame frame39 = new KeyFrame();
        frame39.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
        frame39.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame39.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame39.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
        frame39.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame39.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame39.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame39.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(39, frame39);

        KeyFrame frame20 = new KeyFrame();
        frame20.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
        frame20.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame20.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame20.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
        frame20.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame20.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame20.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame20.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(20, frame20);

        KeyFrame frame10 = new KeyFrame();
        frame10.modelRenderersRotations.put("rightarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
        frame10.modelRenderersRotations.put("rightleg", new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F));
        frame10.modelRenderersRotations.put("leftleg", new Quaternion(0.34202012F, 0.0F, 0.0F, 0.9396926F));
        frame10.modelRenderersRotations.put("leftarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
        frame10.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame10.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame10.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame10.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(10, frame10);

        KeyFrame frame29 = new KeyFrame();
        frame29.modelRenderersRotations.put("rightarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
        frame29.modelRenderersRotations.put("rightleg", new Quaternion(0.34202012F, 0.0F, 0.0F, 0.9396926F));
        frame29.modelRenderersRotations.put("leftleg", new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F));
        frame29.modelRenderersRotations.put("leftarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
        frame29.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame29.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame29.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame29.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(29, frame29);
    }
}