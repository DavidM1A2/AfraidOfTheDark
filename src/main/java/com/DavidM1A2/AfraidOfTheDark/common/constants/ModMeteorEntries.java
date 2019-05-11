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
    public static final MeteorEntry STAR_METAL = new AOTDMeteorEntry("star_metal", new ResourceLocation(Constants.MOD_ID, "textures/gui/star_metal_meteor.png"), 2, 5, 0.4, ModBlocks.STAR_METAL_ORE, ModResearches.ASTRONOMY_2);
    public static final MeteorEntry IGNEOUS = new AOTDMeteorEntry("igneous", new ResourceLocation(Constants.MOD_ID, "textures/gui/sunstone_meteor.png"), 2, 5, 0.4, ModBlocks.SUNSTONE_ORE, ModResearches.ASTRONOMY_2);

    public static final MeteorEntry[] METEOR_ENTRY_LIST = new MeteorEntry[]
            {
                    ASTRAL_SILVER,
                    STAR_METAL,
                    IGNEOUS
            };
}
