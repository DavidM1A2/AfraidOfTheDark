package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.entity.bolt.*
import com.davidm1a2.afraidofthedark.common.registry.bolt.AOTDBoltEntry

/**
 * A static class containing all of our bolt entry references for us
 */
object ModBoltEntries {
    val WOODEN = AOTDBoltEntry(
        "wooden",
        ModItems.WOODEN_BOLT,
        { world, thrower -> EntityWoodenBolt(thrower, world) },
        null
    )
    val IRON = AOTDBoltEntry(
        "iron",
        ModItems.IRON_BOLT,
        { world, thrower -> EntityIronBolt(thrower, world) },
        null
    )
    val ASTRAL_SILVER = AOTDBoltEntry(
        "astral_silver",
        ModItems.SILVER_BOLT,
        { world, thrower -> EntitySilverBolt(thrower, world) },
        ModResearches.ASTRAL_SILVER
    )
    val IGNEOUS = AOTDBoltEntry(
        "igneous",
        ModItems.IGNEOUS_BOLT,
        { world, thrower -> EntityIgneousBolt(thrower, world) },
        ModResearches.IGNEOUS
    )
    val STAR_METAL = AOTDBoltEntry(
        "star_metal",
        ModItems.STAR_METAL_BOLT,
        { world, thrower -> EntityStarMetalBolt(thrower, world) },
        ModResearches.STAR_METAL
    )

    val BOLT_ENTRY_LIST = arrayOf(
        WOODEN,
        IRON,
        ASTRAL_SILVER,
        IGNEOUS,
        STAR_METAL
    )
}