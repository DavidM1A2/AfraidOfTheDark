package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.schematic.Schematic;
import com.DavidM1A2.afraidofthedark.common.schematic.SchematicLoader;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our schematic references for us
 */
public class ModSchematics
{
	public static final Schematic CRYPT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID , "schematics/crypt.schematic"));
}
