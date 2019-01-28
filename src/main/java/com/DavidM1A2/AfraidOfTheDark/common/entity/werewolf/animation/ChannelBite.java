package com.DavidM1A2.afraidofthedark.common.entity.werewolf.animation;

import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.KeyFrame;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

/**
 * Bite animation used by the enchanted skeleton
 */
public class ChannelBite extends Channel
{
	/**
	 * Constructor just calls super with parameters
	 *
	 * @param name The name of the channel
	 * @param fps The FPS of the animation
	 * @param totalFrames The number of frames in the animation
	 * @param mode The animation mode to use
	 */
	ChannelBite(String name, float fps, int totalFrames, byte mode)
	{
		super(name, fps, totalFrames, mode);
	}

	/**
	 * Initializes a map of frame -> position which will be interpolated by the rendering handler
	 *
	 * All code below is created by the MC animator software
	 */
	@Override
	protected void initializeAllFrames()
	{
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("Head", new Quaternion(0.0784591F, 0.0F, 0.0F, 0.9969173F));
		frame0.modelRenderersTranslations.put("Head", new Vector3f(0.0F, 3.0F, 10.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame20 = new KeyFrame();
		frame20.modelRenderersRotations.put("SnoutLower", new Quaternion(0.043619387F, 0.0F, 0.0F, 0.99904823F));
		frame20.modelRenderersRotations.put("Head", new Quaternion(0.077267125F, -0.01362428F, -0.17311287F, 0.9817719F));
		frame20.modelRenderersTranslations.put("SnoutLower", new Vector3f(0.0F, -1.0F, 8.0F));
		frame20.modelRenderersTranslations.put("Head", new Vector3f(0.0F, 3.0F, 10.0F));
		keyFrames.put(20, frame20);

		KeyFrame frame39 = new KeyFrame();
		frame39.modelRenderersRotations.put("SnoutLower", new Quaternion(0.17364818F, 0.0F, 0.0F, 0.9848077F));
		frame39.modelRenderersRotations.put("Head", new Quaternion(0.0784591F, 0.0F, 0.0F, 0.9969173F));
		frame39.modelRenderersTranslations.put("SnoutLower", new Vector3f(0.0F, -1.0F, 8.0F));
		frame39.modelRenderersTranslations.put("Head", new Vector3f(0.0F, 3.0F, 10.0F));
		keyFrames.put(39, frame39);

		KeyFrame frame14 = new KeyFrame();
		frame14.modelRenderersRotations.put("SnoutLower", new Quaternion(0.42261827F, 0.0F, 0.0F, 0.90630776F));
		frame14.modelRenderersTranslations.put("SnoutLower", new Vector3f(0.0F, -1.0F, 8.0F));
		keyFrames.put(14, frame14);

	}
}
