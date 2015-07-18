/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.KeyFrame;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Vector3f;

public class ChannelIdle extends Channel
{
	public ChannelIdle(String _name, float _fps, int _totalFrames, byte _mode)
	{
		super(_name, _fps, _totalFrames, _mode);
	}

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