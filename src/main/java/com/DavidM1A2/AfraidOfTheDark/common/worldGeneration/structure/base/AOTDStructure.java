package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;

/**
 * Base class for all AOTD structures
 */
public abstract class AOTDStructure extends Structure
{
	/**
	 * Structure constructor uses AOTD as the prefix for the registry name
	 *
	 * @param baseName The name of the structure
	 */
	public AOTDStructure(String baseName)
	{
		super();
		this.setRegistryName(Constants.MOD_ID + ":" + baseName);
	}
}
