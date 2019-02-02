package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.registry.meteor.AOTDMeteorEntry;
import com.DavidM1A2.afraidofthedark.common.registry.meteor.MeteorEntry;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our meteor entry references for us
 */
public class ModMeteorEntries
{
	public static final MeteorEntry ASTRAL_SILVER = new AOTDMeteorEntry("astral_silver", new ResourceLocation(Constants.MOD_ID, "textures/gui/astral_silver_meteor.png"), 2, 4, 0.75, ModBlocks.ASTRAL_SILVER_ORE, ModResearches.ASTRONOMY_1);

	public static final MeteorEntry[] METEOR_ENTRY_LIST = new MeteorEntry[]
	{
		ASTRAL_SILVER
	};
}
