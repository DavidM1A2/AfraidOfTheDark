package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.KeyFrame;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Vector3f;

public class ChannelArmthrow extends Channel
{
	public ChannelArmthrow(String _name, float _fps, int _totalFrames, byte _mode)
	{
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames()
	{
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		frame0.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame50 = new KeyFrame();
		frame50.modelRenderersRotations.put("rightarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame50.modelRenderersTranslations.put("rightarm", new Vector3f(-4.0F, -2.0F, 0.0F));
		keyFrames.put(50, frame50);

		KeyFrame frame35 = new KeyFrame();
		frame35.modelRenderersRotations.put("leftarm", new Quaternion(0.4477183F, 0.5392792F, 0.54877454F, 0.45560145F));
		frame35.modelRenderersRotations.put("rightarm", new Quaternion(-0.7302104F, -0.12133739F, -0.161128F, 0.6527693F));
		frame35.modelRenderersTranslations.put("leftarm", new Vector3f(-6.0F, -3.5F, 22.0F));
		frame35.modelRenderersTranslations.put("rightarm", new Vector3f(-3.0F, -1.0F, 2.0F));
		keyFrames.put(35, frame35);

		KeyFrame frame20 = new KeyFrame();
		frame20.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame20.modelRenderersRotations.put("rightarm", new Quaternion(-0.3671902F, 0.59756726F, 0.3179583F, 0.63795555F));
		frame20.modelRenderersTranslations.put("leftarm", new Vector3f(4.0F, -2.0F, 0.0F));
		frame20.modelRenderersTranslations.put("rightarm", new Vector3f(-3.0F, -2.5F, 0.0F));
		keyFrames.put(20, frame20);

		KeyFrame frame23 = new KeyFrame();
		frame23.modelRenderersRotations.put("leftarm", new Quaternion(0.0F, 0.0F, -0.050592944F, 0.99871933F));
		frame23.modelRenderersRotations.put("rightarm", new Quaternion(-0.36719024F, 0.59756726F, 0.3179583F, 0.63795555F));
		frame23.modelRenderersTranslations.put("leftarm", new Vector3f(4.2F, -2.5F, 0.0F));
		frame23.modelRenderersTranslations.put("rightarm", new Vector3f(-3.0F, -3.0F, 0.0F));
		keyFrames.put(23, frame23);

		KeyFrame frame56 = new KeyFrame();
		frame56.modelRenderersRotations.put("leftarm", new Quaternion(-0.10266903F, 0.028632134F, 0.6987209F, 0.7074096F));
		frame56.modelRenderersTranslations.put("leftarm", new Vector3f(-6.0F, -3.5F, 116.9F));
		keyFrames.put(56, frame56);

		KeyFrame frame42 = new KeyFrame();
		frame42.modelRenderersRotations.put("leftarm", new Quaternion(-0.7024243F, -0.7052553F, -0.085041374F, 0.04453233F));
		frame42.modelRenderersTranslations.put("leftarm", new Vector3f(-6.0F, -3.5F, 53.6F));
		keyFrames.put(42, frame42);

		KeyFrame frame60 = new KeyFrame();
		frame60.modelRenderersRotations.put("leftarm", new Quaternion(0.45560145F, 0.54877454F, 0.5392792F, 0.4477183F));
		frame60.modelRenderersTranslations.put("leftarm", new Vector3f(-6.0F, -3.5F, 135.0F));
		keyFrames.put(60, frame60);

		KeyFrame frame47 = new KeyFrame();
		frame47.modelRenderersRotations.put("leftarm", new Quaternion(-0.5366766F, -0.44275555F, 0.46048072F, 0.55127424F));
		frame47.modelRenderersTranslations.put("leftarm", new Vector3f(-6.0F, -3.5F, 76.2F));
		keyFrames.put(47, frame47);

	}
}