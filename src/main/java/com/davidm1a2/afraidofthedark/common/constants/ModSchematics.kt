package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicBuilder
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our schematic references for us
 */
object ModSchematics
{
    ///
    /// Standalone schematics
    ///

    val CRYPT = load("schematics/crypt.schematic")
    val WITCH_HUT = load("schematics/witch_hut.schematic")
    val VOID_CHEST_PORTAL = load("schematics/void_chest_portal.schematic")
    val VOID_CHEST = load("schematics/void_chest.schematic")
    val NIGHTMARE_ISLAND = load("schematics/nightmare_island.schematic")
    val ENARIAS_ALTAR = load("schematics/enarias_altar.schematic")

    ///
    /// Dark Forest structure related schematics
    ///

    val BED_HOUSE = load("schematics/dark_forest/bed_house.schematic")
    val TREE_BRANCHY_TYPE_1 = load("schematics/dark_forest/tree_branchy_type_1.schematic")
    val TREE_BRANCHY_TYPE_2 = load("schematics/dark_forest/tree_branchy_type_2.schematic")
    val TREE_LARGE_CIRCLE = load("schematics/dark_forest/tree_large_circle.schematic")
    val TREE_LARGE_DONUT = load("schematics/dark_forest/tree_large_donut.schematic")
    val TREE_SMALL = load("schematics/dark_forest/tree_small.schematic")
    val DARK_FOREST_TREES = arrayOf(
            TREE_BRANCHY_TYPE_1,
            TREE_BRANCHY_TYPE_2,
            TREE_LARGE_CIRCLE,
            TREE_LARGE_DONUT,
            TREE_SMALL
    )

    val PROP_BUSH_1 = load("schematics/dark_forest/prop_bush_1.schematic")
    val PROP_FALLEN_OVER_LOG = load("schematics/dark_forest/prop_fallen_over_log.schematic")
    val PROP_FENCE_1 = load("schematics/dark_forest/prop_fence_1.schematic")
    val PROP_FENCE_2 = load("schematics/dark_forest/prop_fence_2.schematic")
    val PROP_FOUNTAIN = load("schematics/dark_forest/prop_fountain.schematic")
    val PROP_LOG = load("schematics/dark_forest/prop_log.schematic")
    val PROP_PUMPKIN_1 = load("schematics/dark_forest/prop_pumpkin_1.schematic")
    val PROP_PUMPKIN_2 = load("schematics/dark_forest/prop_pumpkin_2.schematic")
    val PROP_STUMP = load("schematics/dark_forest/prop_stump.schematic")
    val DARK_FOREST_PROPS = arrayOf(
            PROP_BUSH_1,
            PROP_FALLEN_OVER_LOG,
            PROP_FENCE_1,
            PROP_FENCE_2,
            PROP_FOUNTAIN,
            PROP_LOG,
            PROP_PUMPKIN_1,
            PROP_PUMPKIN_2,
            PROP_STUMP
    )

    ///
    /// Gnomish city structure related schematics
    ///

    val ENARIA_LAIR = load("schematics/gnomish_city/enaria_lair.schematic")
    val STAIRWELL = load("schematics/gnomish_city/stairwell.schematic")
    val TUNNEL_EW = load("schematics/gnomish_city/tunnel_ew.schematic")
    val TUNNEL_NS = load("schematics/gnomish_city/tunnel_ns.schematic")
    val ROOM_CAVE = load("schematics/gnomish_city/room_cave.schematic")
    val ROOM_FARM = load("schematics/gnomish_city/room_farm.schematic")
    val ROOM_HOTEL = load("schematics/gnomish_city/room_hotel.schematic")
    val ROOM_MEETING_HALL = load("schematics/gnomish_city/room_meeting_hall.schematic")
    val ROOM_MUSHROOM = load("schematics/gnomish_city/room_mushroom.schematic")
    val ROOM_RUIN = load("schematics/gnomish_city/room_ruin.schematic")
    val ROOM_STAIR_DOWN = load("schematics/gnomish_city/room_stair_down.schematic")
    val ROOM_STAIR_UP = load("schematics/gnomish_city/room_stair_up.schematic")
    val ROOM_TANKS = load("schematics/gnomish_city/room_tanks.schematic")
    val GNOMISH_CITY_ROOMS = arrayOf(
            ROOM_CAVE,
            ROOM_FARM,
            ROOM_HOTEL,
            ROOM_MEETING_HALL,
            ROOM_MUSHROOM,
            ROOM_RUIN,
            ROOM_TANKS
    )

    // A list of all schematics present in AOTD
    val LIST = arrayOf(
            CRYPT,
            WITCH_HUT,
            VOID_CHEST_PORTAL,
            VOID_CHEST,
            NIGHTMARE_ISLAND,
            ENARIAS_ALTAR,
            BED_HOUSE,
            ENARIA_LAIR,
            STAIRWELL,
            TUNNEL_EW,
            TUNNEL_NS,
            *DARK_FOREST_TREES,
            *DARK_FOREST_PROPS,
            *GNOMISH_CITY_ROOMS
    )

    /**
     * Creates a schematic from the file path
     *
     * @param path The path to load from
     * @return The schematic in memory
     */
    private fun load(path: String): Schematic
    {
        return SchematicBuilder()
                .withFile(ResourceLocation(Constants.MOD_ID, path))
                .withCacheEnabled(AfraidOfTheDark.INSTANCE.configurationHandler.cacheStructures)
                .build()
    }
}