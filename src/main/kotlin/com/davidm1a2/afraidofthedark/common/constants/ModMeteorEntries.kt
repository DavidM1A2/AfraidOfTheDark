package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.meteor.AOTDMeteorEntry
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our meteor entry references for us
 */
object ModMeteorEntries {
    val ASTRAL_SILVER = AOTDMeteorEntry(
        "astral_silver",
        ResourceLocation(Constants.MOD_ID, "textures/gui/telescope/astral_silver_meteor.png"),
        3,
        5,
        0.75,
        ModBlocks.ASTRAL_SILVER_ORE,
        ModResearches.ASTRONOMY_1
    )
    val STAR_METAL = AOTDMeteorEntry(
        "star_metal",
        ResourceLocation(Constants.MOD_ID, "textures/gui/telescope/star_metal_meteor.png"),
        3,
        6,
        0.4,
        ModBlocks.STAR_METAL_ORE,
        ModResearches.ASTRONOMY_2
    )
    val IGNEOUS = AOTDMeteorEntry(
        "igneous",
        ResourceLocation(Constants.MOD_ID, "textures/gui/telescope/sunstone_meteor.png"),
        3,
        6,
        0.4,
        ModBlocks.SUNSTONE_ORE,
        ModResearches.ASTRONOMY_2
    )

    val METEOR_ENTRY_LIST = arrayOf(
        ASTRAL_SILVER,
        STAR_METAL,
        IGNEOUS
    )
}