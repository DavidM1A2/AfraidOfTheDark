package com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.animation;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.math.Vector3f;

public class KeyFrame
{
	public HashMap<String, Quaternion> modelRenderersRotations = new HashMap<String, Quaternion>();
	public HashMap<String, Vector3f> modelRenderersTranslations = new HashMap<String, Vector3f>();

	public boolean useBoxInRotations(String boxName)
	{
		return modelRenderersRotations.get(boxName) != null;
	}

	public boolean useBoxInTranslations(String boxName)
	{
		return modelRenderersTranslations.get(boxName) != null;
	}
}