package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.JsonCodecLoader
import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our meteor entry references for us
 */
object ModMeteorEntries {
    val ASTRAL_SILVER = load("astral_silver")
    val STAR_METAL = load("star_metal")
    val IGNEOUS = load("igneous")

    val METEOR_ENTRY_LIST = arrayOf(
        ASTRAL_SILVER,
        STAR_METAL,
        IGNEOUS
    )

    private fun load(name: String): MeteorEntry {
        return JsonCodecLoader.load(ResourceLocation(Constants.MOD_ID, "meteor_entries/$name.json"), MeteorEntry.CODEC)
    }
}