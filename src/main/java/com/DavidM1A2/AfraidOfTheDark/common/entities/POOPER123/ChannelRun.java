package com.DavidM1A2.AfraidOfTheDark.common.entities.POOPER123;

/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.KeyFrame;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Vector3f;

public class ChannelRun extends Channel
{
	public ChannelRun(String _name, float _fps, int _totalFrames, byte _mode)
	{
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames()
	{
		KeyFrame frame21 = new KeyFrame();
		frame21.modelRenderersRotations.put("LeftFrontLeg", new Quaternion(-0.5983246F, 0.0F, 0.0F, 0.8012538F));
		frame21.modelRenderersRotations.put("LeftBackLeg", new Quaternion(0.3632512F, 0.0F, 0.0F, 0.9316912F));
		frame21.modelRenderersRotations.put("LefttFrontFoot", new Quaternion(0.1175374F, 0.0F, 0.0F, 0.99306846F));
		frame21.modelRenderersRotations.put("BodyUpper", new Quaternion(-0.043619387F, 0.0F, 0.0F, 0.99904823F));
		frame21.modelRenderersRotations.put("RightFrontLeg", new Quaternion(-0.529179F, 0.0F, 0.0F, 0.8485102F));
		frame21.modelRenderersRotations.put("RightBackLeg", new Quaternion(0.42498952F, 0.0F, 0.0F, 0.9051983F));
		frame21.modelRenderersRotations.put("RightFrontFoot", new Quaternion(0.15126081F, 0.0F, 0.0F, 0.98849386F));
		frame21.modelRenderersRotations.put("LeftBackLowerLeg", new Quaternion(0.1010563F, 0.0F, 0.0F, 0.99488074F));
		frame21.modelRenderersRotations.put("Head", new Quaternion(0.1175374F, 0.0F, 0.0F, 0.99306846F));
		frame21.modelRenderersRotations.put("BodyLower", new Quaternion(0.050592944F, 0.0F, 0.0F, 0.99871933F));
		frame21.modelRenderersRotations.put("LeftBackFoot", new Quaternion(0.1010563F, 0.0F, 0.0F, 0.99488074F));
		frame21.modelRenderersTranslations.put("LeftFrontLeg", new Vector3f(4.5F, -4.0F, 9.0F));
		frame21.modelRenderersTranslations.put("LeftBackLeg", new Vector3f(3.0F, -3.5F, -11.0F));
		frame21.modelRenderersTranslations.put("LefttFrontFoot", new Vector3f(0.0F, -7.0F, -0.5F));
		frame21.modelRenderersTranslations.put("BodyUpper", new Vector3f(0.0F, 0.0F, 0.0F));
		frame21.modelRenderersTranslations.put("RightFrontLeg", new Vector3f(-4.5F, -4.0F, 9.0F));
		frame21.modelRenderersTranslations.put("RightBackLeg", new Vector3f(-3.0F, -3.5F, -11.0F));
		frame21.modelRenderersTranslations.put("RightFrontFoot", new Vector3f(0.0F, -7.0F, -0.5F));
		frame21.modelRenderersTranslations.put("LeftBackLowerLeg", new Vector3f(0.5F, -5.0F, 0.0F));
		frame21.modelRenderersTranslations.put("Head", new Vector3f(0.0F, 3.0F, 10.0F));
		frame21.modelRenderersTranslations.put("BodyLower", new Vector3f(0.0F, 0.0F, 0.5F));
		frame21.modelRenderersTranslations.put("LeftBackFoot", new Vector3f(-1.0F, -4.0F, 0.0F));
		keyFrames.put(21, frame21);

		KeyFrame frame11 = new KeyFrame();
		frame11.modelRenderersRotations.put("LeftFrontLeg", new Quaternion(0.39474386F, 0.0F, 0.0F, 0.91879123F));
		frame11.modelRenderersRotations.put("LeftBackLeg", new Quaternion(-0.45399052F, 0.0F, 0.0F, 0.8910065F));
		frame11.modelRenderersRotations.put("LefttFrontFoot", new Quaternion(-0.084547415F, 0.0F, 0.0F, 0.9964194F));
		frame11.modelRenderersRotations.put("RightBackFoot", new Quaternion(0.016579868F, 0.0F, 0.0F, 0.99986255F));
		frame11.modelRenderersRotations.put("BodyUpper", new Quaternion(0.043619387F, 0.0F, 0.0F, 0.99904823F));
		frame11.modelRenderersRotations.put("RightFrontLeg", new Quaternion(0.39474386F, 0.0F, 0.0F, 0.91879123F));
		frame11.modelRenderersRotations.put("RightBackLeg", new Quaternion(-0.45554492F, 0.0F, 0.0F, 0.89021283F));
		frame11.modelRenderersRotations.put("RightFrontFoot", new Quaternion(-0.084547415F, 0.0F, 0.0F, 0.9964194F));
		frame11.modelRenderersRotations.put("LeftBackLowerLeg", new Quaternion(0.21643962F, 0.0F, 0.0F, 0.976296F));
		frame11.modelRenderersRotations.put("RightBackLowerLeg", new Quaternion(0.21814324F, 0.0F, 0.0F, 0.97591674F));
		frame11.modelRenderersRotations.put("Head", new Quaternion(0.016579868F, 0.0F, 0.0F, 0.99986255F));
		frame11.modelRenderersRotations.put("BodyLower", new Quaternion(-0.06975647F, 0.0F, 0.0F, 0.9975641F));
		frame11.modelRenderersRotations.put("LeftBackFoot", new Quaternion(0.016579868F, 0.0F, 0.0F, 0.99986255F));
		frame11.modelRenderersTranslations.put("LeftFrontLeg", new Vector3f(4.5F, -4.0F, 9.0F));
		frame11.modelRenderersTranslations.put("LeftBackLeg", new Vector3f(3.0F, -3.5F, -11.0F));
		frame11.modelRenderersTranslations.put("LefttFrontFoot", new Vector3f(0.0F, -7.0F, 0.0F));
		frame11.modelRenderersTranslations.put("RightBackFoot", new Vector3f(-1.0F, -4.0F, 0.0F));
		frame11.modelRenderersTranslations.put("BodyUpper", new Vector3f(0.0F, 0.0F, 0.0F));
		frame11.modelRenderersTranslations.put("RightFrontLeg", new Vector3f(-4.5F, -4.0F, 9.0F));
		frame11.modelRenderersTranslations.put("RightBackLeg", new Vector3f(-3.0F, -3.5F, -11.0F));
		frame11.modelRenderersTranslations.put("RightFrontFoot", new Vector3f(0.0F, -7.0F, 0.0F));
		frame11.modelRenderersTranslations.put("LeftBackLowerLeg", new Vector3f(0.5F, -5.0F, 0.0F));
		frame11.modelRenderersTranslations.put("RightBackLowerLeg", new Vector3f(0.5F, -5.0F, 0.0F));
		frame11.modelRenderersTranslations.put("Head", new Vector3f(0.0F, 3.0F, 10.0F));
		frame11.modelRenderersTranslations.put("BodyLower", new Vector3f(0.0F, 0.0F, 0.5F));
		frame11.modelRenderersTranslations.put("LeftBackFoot", new Vector3f(-1.0F, -4.0F, 0.0F));
		keyFrames.put(11, frame11);

		KeyFrame frame31 = new KeyFrame();
		frame31.modelRenderersRotations.put("LeftFrontLeg", new Quaternion(0.17364818F, 0.0F, 0.0F, 0.9848077F));
		frame31.modelRenderersRotations.put("LeftBackLeg", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame31.modelRenderersRotations.put("LefttFrontFoot", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame31.modelRenderersRotations.put("RightBackFoot", new Quaternion(-0.2079117F, 0.0F, 0.0F, 0.9781476F));
		frame31.modelRenderersRotations.put("BodyUpper", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame31.modelRenderersRotations.put("RightFrontLeg", new Quaternion(0.17364818F, 0.0F, 0.0F, 0.9848077F));
		frame31.modelRenderersRotations.put("RightBackLeg", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame31.modelRenderersRotations.put("RightFrontFoot", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame31.modelRenderersRotations.put("LeftBackLowerLeg", new Quaternion(0.47715878F, 0.0F, 0.0F, 0.8788171F));
		frame31.modelRenderersRotations.put("RightBackLowerLeg", new Quaternion(0.47715878F, 0.0F, 0.0F, 0.8788171F));
		frame31.modelRenderersRotations.put("Head", new Quaternion(0.0784591F, 0.0F, 0.0F, 0.9969173F));
		frame31.modelRenderersRotations.put("BodyLower", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame31.modelRenderersRotations.put("LeftBackFoot", new Quaternion(-0.2079117F, 0.0F, 0.0F, 0.9781476F));
		frame31.modelRenderersTranslations.put("LeftFrontLeg", new Vector3f(4.5F, -4.0F, 9.0F));
		frame31.modelRenderersTranslations.put("LeftBackLeg", new Vector3f(3.0F, -3.5F, -11.0F));
		frame31.modelRenderersTranslations.put("LefttFrontFoot", new Vector3f(0.0F, -7.0F, 0.0F));
		frame31.modelRenderersTranslations.put("RightBackFoot", new Vector3f(-1.0F, -4.0F, 0.0F));
		frame31.modelRenderersTranslations.put("BodyUpper", new Vector3f(0.0F, 0.0F, 0.0F));
		frame31.modelRenderersTranslations.put("RightFrontLeg", new Vector3f(-4.5F, -4.0F, 9.0F));
		frame31.modelRenderersTranslations.put("RightBackLeg", new Vector3f(-3.0F, -3.5F, -11.0F));
		frame31.modelRenderersTranslations.put("RightFrontFoot", new Vector3f(0.0F, -7.0F, 0.0F));
		frame31.modelRenderersTranslations.put("LeftBackLowerLeg", new Vector3f(0.5F, -5.0F, 0.0F));
		frame31.modelRenderersTranslations.put("RightBackLowerLeg", new Vector3f(0.5F, -5.0F, 0.0F));
		frame31.modelRenderersTranslations.put("Head", new Vector3f(0.0F, 3.0F, 10.0F));
		frame31.modelRenderersTranslations.put("BodyLower", new Vector3f(0.0F, 0.0F, 0.0F));
		frame31.modelRenderersTranslations.put("LeftBackFoot", new Vector3f(-1.0F, -4.0F, 0.0F));
		keyFrames.put(31, frame31);

	}
}