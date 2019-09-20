package com.davidm1a2.afraidofthedark.common.entity.enaria.animation;

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

/**
 * Walk animation used by enaria in the overworld
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
        frame0.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(0, frame0);

        KeyFrame frame58 = new KeyFrame();
        frame58.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.0012466135F, 0.0F, 0.9999992F));
        frame58.modelRenderersRotations.put("leftarm", new Quaternion(-0.0062269834F, 0.0F, 0.0F, 0.9999806F));
        frame58.modelRenderersRotations.put("rightleg", new Quaternion(-0.027473606F, 0.0F, 0.0F, 0.9996225F));
        frame58.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame58.modelRenderersRotations.put("rightarm", new Quaternion(0.0062269834F, 0.0F, 0.0F, 0.9999806F));
        frame58.modelRenderersRotations.put("leftleg", new Quaternion(0.027473606F, 0.0F, 0.0F, 0.9996225F));
        frame58.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        frame58.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame58.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame58.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame58.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame58.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(58, frame58);

        KeyFrame frame45 = new KeyFrame();
        frame45.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.017452406F, 0.0F, 0.9998477F));
        frame45.modelRenderersRotations.put("leftarm", new Quaternion(-0.08715574F, 0.0F, 0.0F, 0.9961947F));
        frame45.modelRenderersRotations.put("rightleg", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
        frame45.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.017452406F, 0.0F, 0.9998477F));
        frame45.modelRenderersRotations.put("rightarm", new Quaternion(0.08715574F, 0.0F, 0.0F, 0.9961947F));
        frame45.modelRenderersRotations.put("leftleg", new Quaternion(0.38268346F, 0.0F, 0.0F, 0.9238795F));
        frame45.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        frame45.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame45.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame45.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame45.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame45.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(45, frame45);

        KeyFrame frame30 = new KeyFrame();
        frame30.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame30.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        frame30.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame30.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame30.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame30.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame30.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(30, frame30);

        KeyFrame frame15 = new KeyFrame();
        frame15.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.017452406F, 0.0F, 0.9998477F));
        frame15.modelRenderersRotations.put("leftarm", new Quaternion(0.08715574F, 0.0F, 0.0F, 0.9961947F));
        frame15.modelRenderersRotations.put("rightleg", new Quaternion(0.38268346F, 0.0F, 0.0F, 0.9238795F));
        frame15.modelRenderersRotations.put("head", new Quaternion(0.0F, -0.017452406F, 0.0F, 0.9998477F));
        frame15.modelRenderersRotations.put("rightarm", new Quaternion(-0.08715574F, 0.0F, 0.0F, 0.9961947F));
        frame15.modelRenderersRotations.put("leftleg", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
        frame15.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        frame15.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame15.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame15.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame15.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame15.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        keyFrames.put(15, frame15);
    }
}
