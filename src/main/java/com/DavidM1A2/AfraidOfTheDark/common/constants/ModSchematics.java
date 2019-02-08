package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.Schematic;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicLoader;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our schematic references for us
 */
public class ModSchematics
{
	public static final Schematic CRYPT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID , "schematics/crypt.schematic"));
	public static final Schematic WITCH_HUT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/witch_hut.schematic"));
	public static final Schematic VOID_CHEST_PORTAL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/void_chest_portal.schematic"));
}
