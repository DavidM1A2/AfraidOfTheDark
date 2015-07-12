package com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.KeyFrame;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Vector3f;

public class ChannelWalk extends Channel
{
	public ChannelWalk(String _name, float _fps, int _totalFrames, byte _mode)
	{
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames()
	{
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame0.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame0.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
		frame0.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
		frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame39 = new KeyFrame();
		frame39.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame39.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame39.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame39.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame39.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame39.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
		frame39.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
		frame39.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(39, frame39);

		KeyFrame frame20 = new KeyFrame();
		frame20.modelRenderersRotations.put("rightarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame20.modelRenderersRotations.put("rightleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame20.modelRenderersRotations.put("leftleg", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame20.modelRenderersRotations.put("leftarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame20.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame20.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
		frame20.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
		frame20.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(20, frame20);

		KeyFrame frame10 = new KeyFrame();
		frame10.modelRenderersRotations.put("rightarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame10.modelRenderersRotations.put("rightleg", new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F));
		frame10.modelRenderersRotations.put("leftleg", new Quaternion(0.34202012F, 0.0F, 0.0F, 0.9396926F));
		frame10.modelRenderersRotations.put("leftarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame10.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame10.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
		frame10.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
		frame10.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(10, frame10);

		KeyFrame frame29 = new KeyFrame();
		frame29.modelRenderersRotations.put("rightarm", new Quaternion(-0.7372773F, 0.0F, 0.0F, 0.6755902F));
		frame29.modelRenderersRotations.put("rightleg", new Quaternion(0.34202012F, 0.0F, 0.0F, 0.9396926F));
		frame29.modelRenderersRotations.put("leftleg", new Quaternion(-0.34202012F, 0.0F, 0.0F, 0.9396926F));
		frame29.modelRenderersRotations.put("leftarm", new Quaternion(-0.67559016F, 0.0F, 0.0F, 0.7372773F));
		frame29.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame29.modelRenderersTranslations.put("rightleg", new Vector3f(-2.0F, -12.0F, 0.0F));
		frame29.modelRenderersTranslations.put("leftleg", new Vector3f(2.0F, -12.0F, 0.0F));
		frame29.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		keyFrames.put(29, frame29);

	}
}