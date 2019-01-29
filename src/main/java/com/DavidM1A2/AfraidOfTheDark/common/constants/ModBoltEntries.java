package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityIronBolt;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityWoodenBolt;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.AOTDBoltEntry;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;

/**
 * A static class containing all of our bolt entry references for us
 */
public class ModBoltEntries
{
	public static final BoltEntry WOODEN = new AOTDBoltEntry("wooden", ModItems.WOODEN_BOLT, EntityWoodenBolt::new, null);
	public static final BoltEntry IRON = new AOTDBoltEntry("iron", ModItems.IRON_BOLT, EntityIronBolt::new, null);

	public static final BoltEntry[] BOLT_ENTRY_LIST = new BoltEntry[]
	{
		WOODEN,
		IRON
	};
}
