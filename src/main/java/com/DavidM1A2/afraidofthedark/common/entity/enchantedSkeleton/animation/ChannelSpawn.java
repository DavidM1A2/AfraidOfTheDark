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
 * Spawn animation used by the enchanted skeleton
 */
public class ChannelSpawn extends Channel
{
	/**
	 * Constructor just calls super with parameters
	 *
	 * @param name The name of the channel
	 * @param fps The FPS of the animation
	 * @param totalFrames The number of frames in the animation
	 * @param mode The animation mode to use
	 */
	ChannelSpawn(String name, float fps, int totalFrames, byte mode)
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
		frame0.modelRenderersRotations.put("rightarm", new Quaternion(0.1830127F, -0.1830127F, -0.68301266F, 0.68301266F));
		frame0.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, -0.70710677F, 0.70710677F));
		frame0.modelRenderersRotations.put("leftleg", new Quaternion(-0.70710677F, 0.0F, 0.0F, 0.70710677F));
		frame0.modelRenderersRotations.put("head", new Quaternion(-0.28985932F, 0.28985932F, 0.6449663F, -0.6449663F));
		frame0.modelRenderersRotations.put("heart", new Quaternion(0.0F, -0.7906896F, 0.0F, 0.61221725F));
		frame0.modelRenderersRotations.put("leftarm", new Quaternion(0.47314674F, -0.47314674F, -0.5254827F, 0.5254827F));
		frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-5.0F, -24.0F, 9.0F));
		frame0.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -23.0F, 0.0F));
		frame0.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -23.0F, 7.0F));
		frame0.modelRenderersTranslations.put("head", new Vector3f(10.0F, -20.0F, -13.0F));
		frame0.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -22.0F, 0.0F));
		frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -22.0F, 0.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame17 = new KeyFrame();
		frame17.modelRenderersRotations.put("leftleg", new Quaternion(-0.70710677F, 0.0F, 0.0F, 0.70710677F));
		frame17.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -23.0F, -0.100000024F));
		keyFrames.put(17, frame17);

		KeyFrame frame34 = new KeyFrame();
		frame34.modelRenderersRotations.put("head", new Quaternion(-0.7247734F, 0.0F, 0.0F, 0.6889873F));
		frame34.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(34, frame34);

		KeyFrame frame33 = new KeyFrame();
		frame33.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame33.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame33.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame33.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(33, frame33);

		KeyFrame frame21 = new KeyFrame();
		frame21.modelRenderersRotations.put("heart", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame21.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -3.0F, 0.0F));
		keyFrames.put(21, frame21);

		KeyFrame frame39 = new KeyFrame();
		frame39.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame39.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame39.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame39.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame39.modelRenderersRotations.put("heart", new Quaternion(0.0F, 1.0F, 0.0F, -4.371139E-8F));
		frame39.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame39.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame39.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame39.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
		frame39.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
		frame39.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
		frame39.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -3.0F, 0.0F));
		frame39.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
		frame39.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(39, frame39);

		KeyFrame frame31 = new KeyFrame();
		frame31.modelRenderersRotations.put("heart", new Quaternion(0.0F, 0.88253754F, 0.0F, 0.4702419F));
		frame31.modelRenderersTranslations.put("heart", new Vector3f(0.0F, -3.0F, 0.0F));
		keyFrames.put(31, frame31);

	}
}