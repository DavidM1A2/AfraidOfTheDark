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

public class ChannelAttack extends Channel
{
	public ChannelAttack(String _name, float _fps, int _totalFrames, byte _mode)
	{
		super(_name, _fps, _totalFrames, _mode);
	}

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