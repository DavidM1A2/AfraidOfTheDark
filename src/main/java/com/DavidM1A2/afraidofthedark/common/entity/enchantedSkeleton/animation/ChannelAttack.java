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
 * Attack animation used by the enchanted skeleton
 */
public class ChannelAttack extends Channel
{
	/**
	 * Constructor just calls super with parameters
	 *
	 * @param name The name of the channel
	 * @param fps The FPS of the animation
	 * @param totalFrames The number of frames in the animation
	 * @param mode The animation mode to use
	 */
	ChannelAttack(String name, float fps, int totalFrames, byte mode)
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
		frame0.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame0.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame19 = new KeyFrame();
		frame19.modelRenderersRotations.put("rightarm", new Quaternion(-0.9542403F, 0.0F, 0.0F, 0.2990408F));
		frame19.modelRenderersRotations.put("leftarm", new Quaternion(-0.9378889F, 0.0F, 0.0F, 0.3469357F));
		frame19.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame19.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(19, frame19);

	}
}