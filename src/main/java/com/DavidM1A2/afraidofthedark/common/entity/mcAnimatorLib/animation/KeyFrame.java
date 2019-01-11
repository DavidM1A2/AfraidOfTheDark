package com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation;

import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * Class was provided by the MC animator library
 */
public class KeyFrame
{
	public Map<String, Quaternion> modelRenderersRotations = new HashMap<>();
	public Map<String, Vector3f> modelRenderersTranslations = new HashMap<>();

	public boolean useBoxInRotations(String boxName)
	{
		return modelRenderersRotations.get(boxName) != null;
	}

	public boolean useBoxInTranslations(String boxName)
	{
		return modelRenderersTranslations.get(boxName) != null;
	}
}