package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.JsonCodecLoader
import com.davidm1a2.afraidofthedark.common.registry.bolt.BoltEntry
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our bolt entry references for us
 */
object ModBoltEntries {
    val WOODEN = load("wooden")
    val IRON = load("iron")
    val ASTRAL_SILVER = load("astral_silver")
    val IGNEOUS = load("igneous")
    val STAR_METAL = load("star_metal")

    val BOLT_ENTRY_LIST = arrayOf(
        WOODEN,
        IRON,
        ASTRAL_SILVER,
        IGNEOUS,
        STAR_METAL
    )

    private fun load(name: String): BoltEntry {
        return JsonCodecLoader.load(ResourceLocation(Constants.MOD_ID, "bolt_entries/$name.json"), BoltEntry.CODEC)
    }
}