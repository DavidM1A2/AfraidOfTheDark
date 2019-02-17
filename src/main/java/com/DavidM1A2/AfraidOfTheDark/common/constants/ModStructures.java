package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.StructureCrypt;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.StructureVoidChest;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.StructureWitchHut;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;

/**
 * A list of structures to be registered
 */
public class ModStructures
{
	public static final Structure CRYPT = new StructureCrypt();
	public static final Structure WITCH_HUT = new StructureWitchHut();
	public static final Structure VOID_CHEST = new StructureVoidChest();

	public static Structure[] STRUCTURE_LIST = new Structure[]
	{
		CRYPT,
		WITCH_HUT,
		VOID_CHEST
	};
}
