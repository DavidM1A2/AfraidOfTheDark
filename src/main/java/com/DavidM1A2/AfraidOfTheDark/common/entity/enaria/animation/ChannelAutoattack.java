package com.DavidM1A2.afraidofthedark.common.entity.enaria.animation;

import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

/**
 * Auto attack animation used by enaria in the overworld
 */
public class ChannelAutoattack extends Channel
{
    /**
     * Constructor just calls super with parameters
     *
     * @param name        The name of the channel
     * @param fps         The FPS of the animation
     * @param totalFrames The number of frames in the animation
     * @param mode        The animation mode to use
     */
    ChannelAutoattack(String name, float fps, int totalFrames, byte mode)
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
        frame0.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(0, frame0);

        KeyFrame frame50 = new KeyFrame();
        frame50.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame50.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame50.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame50.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame50.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame50.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        keyFrames.put(50, frame50);

        KeyFrame frame30 = new KeyFrame();
        frame30.modelRenderersRotations.put("leftarm", new Quaternion(-0.6889273F, -0.17649558F, 0.13222496F, 0.69046724F));
        frame30.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.06975647F, 0.9975641F));
        frame30.modelRenderersRotations.put("rightarm", new Quaternion(-0.6543772F, -0.0049159825F, 0.17740688F, 0.7350463F));
        frame30.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersTranslations.put("leftarm", new Vector3f(2.0F, -2.5F, 0.0F));
        frame30.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame30.modelRenderersTranslations.put("head", new Vector3f(0.0F, -0.5F, 0.0F));
        frame30.modelRenderersTranslations.put("rightarm", new Vector3f(-2.0F, -2.0F, 0.0F));
        frame30.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(30, frame30);
    }
}