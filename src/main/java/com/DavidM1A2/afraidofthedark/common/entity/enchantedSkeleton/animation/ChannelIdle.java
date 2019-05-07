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
 * Idle animation used by the enchanted skeleton
 */
public class ChannelIdle extends Channel
{
    /**
     * Constructor just calls super with parameters
     *
     * @param name        The name of the channel
     * @param fps         The FPS of the animation
     * @param totalFrames The number of frames in the animation
     * @param mode        The animation mode to use
     */
    ChannelIdle(String name, float fps, int totalFrames, byte mode)
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
        frame0.modelRenderersRotations.put("rightarm", new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F));
        frame0.modelRenderersRotations.put("heart", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("leftarm", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
        frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -3.0F, 0.0F));
        frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(0, frame0);

        KeyFrame frame19 = new KeyFrame();
        frame19.modelRenderersRotations.put("rightarm", new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F));
        frame19.modelRenderersRotations.put("heart", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame19.modelRenderersRotations.put("leftarm", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
        frame19.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame19.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -3.0F, 0.0F));
        frame19.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(19, frame19);

        KeyFrame frame9 = new KeyFrame();
        frame9.modelRenderersRotations.put("rightarm", new Quaternion(-0.35836795F, 0.0F, 0.0F, 0.9335804F));
        frame9.modelRenderersRotations.put("heart", new Quaternion(0.0F, 1.0F, 0.0F, -4.371139E-8F));
        frame9.modelRenderersRotations.put("leftarm", new Quaternion(-0.39874908F, 0.0F, 0.0F, 0.9170601F));
        frame9.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame9.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -3.0F, 0.0F));
        frame9.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        keyFrames.put(9, frame9);

    }
}