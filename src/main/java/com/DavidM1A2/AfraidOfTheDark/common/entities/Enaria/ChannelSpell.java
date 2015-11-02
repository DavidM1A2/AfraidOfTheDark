package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.KeyFrame;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Vector3f;

public class ChannelSpell extends Channel
{
	public ChannelSpell(String _name, float _fps, int _totalFrames, byte _mode)
	{
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames()
	{
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		frame0.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame80 = new KeyFrame();
		frame80.modelRenderersRotations.put("body", new Quaternion(0.0F, 1.0F, 0.0F, -4.371139E-8F));
		frame80.modelRenderersTranslations.put("body", new Vector3f(0.0F, 32.0F, 2.0F));
		keyFrames.put(80, frame80);

		KeyFrame frame20 = new KeyFrame();
		frame20.modelRenderersRotations.put("leftarm", new Quaternion(-0.25F, 0.0669873F, -0.25F, 0.93301266F));
		frame20.modelRenderersRotations.put("rightarm", new Quaternion(-0.25F, -0.0669873F, 0.25F, 0.93301266F));
		frame20.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame20.modelRenderersTranslations.put("leftarm", new Vector3f(3.0F, -2.0F, 0.0F));
		frame20.modelRenderersTranslations.put("rightarm", new Vector3f(-3.0F, -2.0F, 0.0F));
		frame20.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
		keyFrames.put(20, frame20);

		KeyFrame frame100 = new KeyFrame();
		frame100.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.70710677F, 0.70710677F));
		frame100.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, -0.70710677F, 0.70710677F));
		frame100.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame100.modelRenderersTranslations.put("leftarm", new Vector3f(6.0F, -2.0F, 0.0F));
		frame100.modelRenderersTranslations.put("rightarm", new Vector3f(-6.0F, -2.0F, 0.0F));
		frame100.modelRenderersTranslations.put("body", new Vector3f(0.0F, 32.0F, 2.0F));
		keyFrames.put(100, frame100);

		KeyFrame frame70 = new KeyFrame();
		frame70.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
		frame70.modelRenderersTranslations.put("body", new Vector3f(0.0F, 32.0F, 2.0F));
		keyFrames.put(70, frame70);

		KeyFrame frame90 = new KeyFrame();
		frame90.modelRenderersRotations.put("body", new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
		frame90.modelRenderersTranslations.put("body", new Vector3f(0.0F, 32.0F, 2.0F));
		keyFrames.put(90, frame90);

		KeyFrame frame60 = new KeyFrame();
		frame60.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.70710677F, 0.70710677F));
		frame60.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, -0.70710677F, 0.70710677F));
		frame60.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame60.modelRenderersTranslations.put("leftarm", new Vector3f(6.0F, -2.0F, 0.0F));
		frame60.modelRenderersTranslations.put("rightarm", new Vector3f(-6.0F, -2.0F, 0.0F));
		frame60.modelRenderersTranslations.put("body", new Vector3f(0.0F, 32.0F, 2.0F));
		keyFrames.put(60, frame60);

		KeyFrame frame110 = new KeyFrame();
		frame110.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame110.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame110.modelRenderersRotations.put("body", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame110.modelRenderersTranslations.put("leftarm", new Vector3f(6.0F, -2.0F, 0.0F));
		frame110.modelRenderersTranslations.put("rightarm", new Vector3f(-6.0F, -2.0F, 0.0F));
		frame110.modelRenderersTranslations.put("body", new Vector3f(0.0F, 2.0F, 2.0F));
		keyFrames.put(110, frame110);

	}
}