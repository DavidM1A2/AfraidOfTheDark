package com.davidm1a2.afraidofthedark.common.entity.enaria.animation;

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

/**
 * Dance animation used by enaria in the nightmare realm
 */
public class ChannelDance extends Channel
{
    /**
     * Constructor just calls super with parameters
     *
     * @param name        The name of the channel
     * @param fps         The FPS of the animation
     * @param totalFrames The number of frames in the animation
     * @param mode        The animation mode to use
     */
    public ChannelDance(String name, float fps, int totalFrames, byte mode)
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
        frame0.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame0.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(0, frame0);

        KeyFrame frame130 = new KeyFrame();
        frame130.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame130.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame130.modelRenderersRotations.put("head", new Quaternion(0.019156948F, -0.3781637F, -0.04682582F, 0.9243552F));
        frame130.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame130.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame130.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.70090926F, 0.0F, 0.71325046F));
        frame130.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame130.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame130.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame130.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame130.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame130.modelRenderersTranslations.put("body", new Vector3f(-30.0F, 2.0F, 2.0F));
        keyFrames.put(130, frame130);

        KeyFrame frame135 = new KeyFrame();
        frame135.modelRenderersRotations.put("head", new Quaternion(0.0F, -0.5714299F, 0.0F, 0.8206509F));
        frame135.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        keyFrames.put(135, frame135);

        KeyFrame frame170 = new KeyFrame();
        frame170.modelRenderersRotations.put("leftarm", new Quaternion(-0.3313379F, 0.0F, 0.0F, 0.94351214F));
        frame170.modelRenderersRotations.put("rightleg", new Quaternion(-0.39341342F, 0.0047545787F, -0.053014386F, 0.91781956F));
        frame170.modelRenderersRotations.put("rightarm", new Quaternion(0.3313379F, 0.0F, 0.0F, 0.94351214F));
        frame170.modelRenderersRotations.put("leftleg", new Quaternion(0.393853F, 0.026504928F, 0.06169189F, 0.91671777F));
        frame170.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame170.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame170.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame170.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame170.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame170.modelRenderersTranslations.put("body", new Vector3f(-15.0F, 12.0F, 2.0F));
        keyFrames.put(170, frame170);

        KeyFrame frame235 = new KeyFrame();
        frame235.modelRenderersRotations.put("leftarm", new Quaternion(-0.089179166F, -0.023430947F, 0.4556857F, 0.88535225F));
        frame235.modelRenderersRotations.put("rightarm", new Quaternion(-0.15583336F, 0.11884418F, -0.4869865F, 0.8511381F));
        frame235.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame235.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        keyFrames.put(235, frame235);

        KeyFrame frame140 = new KeyFrame();
        frame140.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame140.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame140.modelRenderersRotations.put("body", new Quaternion(0.0F, -1.0F, 0.0F, -4.371139E-8F));
        frame140.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame140.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame140.modelRenderersTranslations.put("body", new Vector3f(-30.0F, 2.0F, 2.0F));
        keyFrames.put(140, frame140);

        KeyFrame frame204 = new KeyFrame();
        frame204.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.70090926F, 0.71325046F));
        frame204.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, -0.71325046F, 0.70090926F));
        frame204.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame204.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        keyFrames.put(204, frame204);

        KeyFrame frame110 = new KeyFrame();
        frame110.modelRenderersRotations.put("leftarm", new Quaternion(-0.0618741F, -0.1637141F, 0.4520731F, 0.8746424F));
        frame110.modelRenderersRotations.put("rightleg", new Quaternion(-0.40992305F, 0.0F, 0.0F, 0.9121201F));
        frame110.modelRenderersRotations.put("head", new Quaternion(-0.1328944F, 0.045519046F, 0.040491916F, 0.989256F));
        frame110.modelRenderersRotations.put("rightarm", new Quaternion(-0.11502661F, -0.02034384F, -0.5898031F, 0.799054F));
        frame110.modelRenderersRotations.put("leftleg", new Quaternion(0.34693563F, 0.0F, 0.0F, 0.9378889F));
        frame110.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.70090926F, 0.0F, 0.71325046F));
        frame110.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame110.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame110.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame110.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame110.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame110.modelRenderersTranslations.put("body", new Vector3f(-17.0F, 12.0F, 2.0F));
        keyFrames.put(110, frame110);

        KeyFrame frame240 = new KeyFrame();
        frame240.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame240.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame240.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame240.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame240.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame240.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(240, frame240);

        KeyFrame frame150 = new KeyFrame();
        frame150.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame150.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame150.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame150.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame150.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame150.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame150.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame150.modelRenderersTranslations.put("body", new Vector3f(-30.0F, 2.0F, 2.0F));
        keyFrames.put(150, frame150);

        KeyFrame frame280 = new KeyFrame();
        frame280.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame280.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame280.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame280.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame280.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame280.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame280.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame280.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame280.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
        frame280.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame280.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame280.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(280, frame280);

        KeyFrame frame90 = new KeyFrame();
        frame90.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame90.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame90.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame90.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame90.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.70090926F, 0.0F, 0.71325046F));
        frame90.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame90.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame90.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame90.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame90.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(90, frame90);

        KeyFrame frame60 = new KeyFrame();
        frame60.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame60.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame60.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame60.modelRenderersRotations.put("body", new Quaternion(0.0F, 8.726645E-4F, 0.0F, 0.99999964F));
        frame60.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame60.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame60.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame60.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(60, frame60);

        KeyFrame frame220 = new KeyFrame();
        frame220.modelRenderersRotations.put("body", new Quaternion(0.0F, -1.0F, 0.0F, -4.371139E-8F));
        frame220.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(220, frame220);

        KeyFrame frame61 = new KeyFrame();
        frame61.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame61.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(61, frame61);

        KeyFrame frame221 = new KeyFrame();
        frame221.modelRenderersRotations.put("leftarm", new Quaternion(-0.0044424776F, 0.0044424776F, 0.63891906F, 0.7692483F));
        frame221.modelRenderersRotations.put("rightarm", new Quaternion(-0.07202796F, 0.07084146F, -0.68537843F, 0.72114486F));
        frame221.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.9994209F, 0.0F, 0.034027282F));
        frame221.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame221.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame221.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(221, frame221);

        KeyFrame frame30 = new KeyFrame();
        frame30.modelRenderersRotations.put("leftarm", new Quaternion(-0.9928517F, -0.08424469F, 0.08424469F, 0.007148222F));
        frame30.modelRenderersRotations.put("rightleg", new Quaternion(0.7221783F, 0.058252096F, -0.061277717F, 0.68652034F));
        frame30.modelRenderersRotations.put("rightarm", new Quaternion(-0.7582637F, 0.025816685F, -0.6510593F, 0.022166627F));
        frame30.modelRenderersRotations.put("body", new Quaternion(0.0F, 1.0F, 0.0F, -4.371139E-8F));
        frame30.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame30.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame30.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame30.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(30, frame30);

        KeyFrame frame190 = new KeyFrame();
        frame190.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame190.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame190.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame190.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame190.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame190.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
        frame190.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
        frame190.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
        frame190.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
        frame190.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
        keyFrames.put(190, frame190);

    }
}
