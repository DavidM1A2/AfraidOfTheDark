package com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.animation;

import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

/**
 * Activate animation used by the splinter drone
 */
public class ChannelActivate extends Channel
{
    /**
     * Constructor just calls super with parameters
     *
     * @param name        The name of the channel
     * @param fps         The FPS of the animation
     * @param totalFrames The number of frames in the animation
     * @param mode        The animation mode to use
     */
    ChannelActivate(String name, float fps, int totalFrames, byte mode)
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
        frame0.modelRenderersRotations.put("Pillar1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("BottomPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("Sphere2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("Sphere1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("TopPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame0.modelRenderersRotations.put("Pillar2", new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F));
        frame0.modelRenderersRotations.put("Pillar6", new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F));
        frame0.modelRenderersRotations.put("Pillar7", new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
        frame0.modelRenderersRotations.put("Pillar8", new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F));
        frame0.modelRenderersRotations.put("Pillar4", new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F));
        frame0.modelRenderersRotations.put("Pillar3", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame0.modelRenderersRotations.put("Pillar5", new Quaternion(0.0F, 1.0F, 0.0F, 7.54979E-8F));
        frame0.modelRenderersTranslations.put("Pillar1", new Vector3f(0.0F, 0.0F, -3.0F));
        frame0.modelRenderersTranslations.put("BottomPlate", new Vector3f(0.0F, -12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("Sphere2", new Vector3f(0.0F, 11.0F, 0.0F));
        frame0.modelRenderersTranslations.put("Sphere1", new Vector3f(0.0F, -11.0F, 0.0F));
        frame0.modelRenderersTranslations.put("TopPlate", new Vector3f(0.0F, 12.0F, 0.0F));
        frame0.modelRenderersTranslations.put("Pillar2", new Vector3f(-3.5F, 0.0F, -3.5F));
        frame0.modelRenderersTranslations.put("Pillar6", new Vector3f(3.5F, 0.0F, 3.5F));
        frame0.modelRenderersTranslations.put("Pillar7", new Vector3f(3.0F, 0.0F, 0.0F));
        frame0.modelRenderersTranslations.put("Pillar8", new Vector3f(3.5F, 0.0F, -3.5F));
        frame0.modelRenderersTranslations.put("Pillar4", new Vector3f(-3.5F, 0.0F, 3.5F));
        frame0.modelRenderersTranslations.put("Pillar3", new Vector3f(-3.0F, 0.0F, 0.0F));
        frame0.modelRenderersTranslations.put("Pillar5", new Vector3f(0.0F, 0.0F, 3.0F));
        keyFrames.put(0, frame0);

        KeyFrame frame1 = new KeyFrame();
        frame1.modelRenderersRotations.put("Pillar1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame1.modelRenderersRotations.put("BottomPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame1.modelRenderersRotations.put("Sphere2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame1.modelRenderersRotations.put("Sphere1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame1.modelRenderersRotations.put("TopPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame1.modelRenderersRotations.put("Pillar2", new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F));
        frame1.modelRenderersRotations.put("Pillar6", new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F));
        frame1.modelRenderersRotations.put("Pillar7", new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
        frame1.modelRenderersRotations.put("Pillar8", new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F));
        frame1.modelRenderersRotations.put("Pillar4", new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F));
        frame1.modelRenderersRotations.put("Pillar3", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame1.modelRenderersRotations.put("Pillar5", new Quaternion(0.0F, 1.0F, 0.0F, 7.54979E-8F));
        frame1.modelRenderersTranslations.put("Pillar1", new Vector3f(0.0F, 0.0F, -3.0F));
        frame1.modelRenderersTranslations.put("BottomPlate", new Vector3f(0.0F, -12.0F, 0.0F));
        frame1.modelRenderersTranslations.put("Sphere2", new Vector3f(0.0F, 11.0F, 0.0F));
        frame1.modelRenderersTranslations.put("Sphere1", new Vector3f(0.0F, -11.0F, 0.0F));
        frame1.modelRenderersTranslations.put("TopPlate", new Vector3f(0.0F, 12.0F, 0.0F));
        frame1.modelRenderersTranslations.put("Pillar2", new Vector3f(-3.5F, 0.0F, -3.5F));
        frame1.modelRenderersTranslations.put("Pillar6", new Vector3f(3.5F, 0.0F, 3.5F));
        frame1.modelRenderersTranslations.put("Pillar7", new Vector3f(3.0F, 0.0F, 0.0F));
        frame1.modelRenderersTranslations.put("Pillar8", new Vector3f(3.5F, 0.0F, -3.5F));
        frame1.modelRenderersTranslations.put("Pillar4", new Vector3f(-3.5F, 0.0F, 3.5F));
        frame1.modelRenderersTranslations.put("Pillar3", new Vector3f(-3.0F, 0.0F, 0.0F));
        frame1.modelRenderersTranslations.put("Pillar5", new Vector3f(0.0F, 0.0F, 3.0F));
        keyFrames.put(1, frame1);

        KeyFrame frame86 = new KeyFrame();
        frame86.modelRenderersRotations.put("Sphere2", new Quaternion(-1.0F, 0.0F, 0.0F, -4.371139E-8F));
        frame86.modelRenderersRotations.put("Sphere1", new Quaternion(-1.0F, 0.0F, 0.0F, -4.371139E-8F));
        frame86.modelRenderersTranslations.put("Sphere2", new Vector3f(0.0F, 0.0F, 0.0F));
        frame86.modelRenderersTranslations.put("Sphere1", new Vector3f(0.0F, 0.0F, 0.0F));
        keyFrames.put(86, frame86);

        KeyFrame frame99 = new KeyFrame();
        frame99.modelRenderersRotations.put("Pillar1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame99.modelRenderersRotations.put("BottomPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame99.modelRenderersRotations.put("Sphere2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame99.modelRenderersRotations.put("Sphere1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame99.modelRenderersRotations.put("TopPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame99.modelRenderersRotations.put("Pillar2", new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F));
        frame99.modelRenderersRotations.put("Pillar6", new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F));
        frame99.modelRenderersRotations.put("Pillar7", new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
        frame99.modelRenderersRotations.put("Pillar8", new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F));
        frame99.modelRenderersRotations.put("Pillar4", new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F));
        frame99.modelRenderersRotations.put("Pillar3", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame99.modelRenderersRotations.put("Pillar5", new Quaternion(0.0F, -1.0F, 0.0F, -4.371139E-8F));
        frame99.modelRenderersTranslations.put("Pillar1", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("BottomPlate", new Vector3f(0.0F, -12.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Sphere2", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Sphere1", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("TopPlate", new Vector3f(0.0F, 12.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar2", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar6", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar7", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar8", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar4", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar3", new Vector3f(0.0F, 0.0F, 0.0F));
        frame99.modelRenderersTranslations.put("Pillar5", new Vector3f(0.0F, 0.0F, 0.0F));
        keyFrames.put(99, frame99);

        KeyFrame frame24 = new KeyFrame();
        frame24.modelRenderersRotations.put("Pillar1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame24.modelRenderersRotations.put("Sphere2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame24.modelRenderersRotations.put("Sphere1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame24.modelRenderersRotations.put("TopPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame24.modelRenderersRotations.put("Pillar2", new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F));
        frame24.modelRenderersRotations.put("Pillar6", new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F));
        frame24.modelRenderersRotations.put("Pillar7", new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
        frame24.modelRenderersRotations.put("Pillar8", new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F));
        frame24.modelRenderersRotations.put("Pillar4", new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F));
        frame24.modelRenderersRotations.put("Pillar3", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame24.modelRenderersRotations.put("Pillar5", new Quaternion(0.0F, 1.0F, 0.0F, 7.54979E-8F));
        frame24.modelRenderersTranslations.put("Pillar1", new Vector3f(0.0F, 0.0F, -3.0F));
        frame24.modelRenderersTranslations.put("Sphere2", new Vector3f(0.0F, 0.0F, 0.0F));
        frame24.modelRenderersTranslations.put("Sphere1", new Vector3f(0.0F, 0.0F, 0.0F));
        frame24.modelRenderersTranslations.put("TopPlate", new Vector3f(0.0F, 12.0F, 0.0F));
        frame24.modelRenderersTranslations.put("Pillar2", new Vector3f(-3.5F, 0.0F, -3.5F));
        frame24.modelRenderersTranslations.put("Pillar6", new Vector3f(3.5F, 0.0F, 3.5F));
        frame24.modelRenderersTranslations.put("Pillar7", new Vector3f(3.0F, 0.0F, 0.0F));
        frame24.modelRenderersTranslations.put("Pillar8", new Vector3f(3.5F, 0.0F, -3.5F));
        frame24.modelRenderersTranslations.put("Pillar4", new Vector3f(-3.5F, 0.0F, 3.5F));
        frame24.modelRenderersTranslations.put("Pillar3", new Vector3f(-3.0F, 0.0F, 0.0F));
        frame24.modelRenderersTranslations.put("Pillar5", new Vector3f(0.0F, 0.0F, 3.0F));
        keyFrames.put(24, frame24);

        KeyFrame frame73 = new KeyFrame();
        frame73.modelRenderersRotations.put("Pillar1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame73.modelRenderersRotations.put("BottomPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame73.modelRenderersRotations.put("Sphere2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame73.modelRenderersRotations.put("Sphere1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame73.modelRenderersRotations.put("TopPlate", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
        frame73.modelRenderersRotations.put("Pillar2", new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F));
        frame73.modelRenderersRotations.put("Pillar6", new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F));
        frame73.modelRenderersRotations.put("Pillar7", new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
        frame73.modelRenderersRotations.put("Pillar8", new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F));
        frame73.modelRenderersRotations.put("Pillar4", new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F));
        frame73.modelRenderersRotations.put("Pillar3", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
        frame73.modelRenderersRotations.put("Pillar5", new Quaternion(0.0F, 1.0F, 0.0F, 7.54979E-8F));
        frame73.modelRenderersTranslations.put("Pillar1", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("BottomPlate", new Vector3f(0.0F, -12.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Sphere2", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Sphere1", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("TopPlate", new Vector3f(0.0F, 12.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar2", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar6", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar7", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar8", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar4", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar3", new Vector3f(0.0F, 0.0F, 0.0F));
        frame73.modelRenderersTranslations.put("Pillar5", new Vector3f(0.0F, 0.0F, 0.0F));
        keyFrames.put(73, frame73);

    }
}
