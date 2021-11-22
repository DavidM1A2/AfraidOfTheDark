package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.schematic.SchematicBuilder
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our schematic references for us
 */
object ModSchematics {
    ///
    /// Standalone schematics
    ///

    val CRYPT = load("schematics/crypt.schematic")
    val WITCH_HUT = load("schematics/witch_hut.schematic")
    val VOID_CHEST_PORTAL = load("schematics/void_chest_portal.schematic")
    val VOID_CHEST = load("schematics/void_chest.schematic")
    val NIGHTMARE_ISLAND = load("schematics/nightmare_island.schematic")
    val ENARIAS_ALTAR = load("schematics/enarias_altar.schematic")
    val OBSERVATORY = load("schematics/observatory.schematic")
    val ALTAR_RUINS = load("schematics/altar_ruins.schematic")

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
    /// Forbidden city structure related schematics
    ///

    val ENARIA_LAIR = load("schematics/forbidden_city/enaria_lair.schematic")
    val STAIRWELL = load("schematics/forbidden_city/stairwell.schematic")
    val CONNECTOR = load("schematics/forbidden_city/connector.schematic")
    val ROOM_CAVE = load("schematics/forbidden_city/room_cave.schematic")
    val ROOM_FARM = load("schematics/forbidden_city/room_farm.schematic")
    val ROOM_HOTEL = load("schematics/forbidden_city/room_hotel.schematic")
    val ROOM_MEETING_HALL = load("schematics/forbidden_city/room_meeting_hall.schematic")
    val ROOM_MUSHROOM = load("schematics/forbidden_city/room_mushroom.schematic")
    val ROOM_RUIN = load("schematics/forbidden_city/room_ruin.schematic")
    val ROOM_STAIR_DOWN = load("schematics/forbidden_city/room_stair_down.schematic")
    val ROOM_STAIR_UP = load("schematics/forbidden_city/room_stair_up.schematic")
    val ROOM_TANKS = load("schematics/forbidden_city/room_tanks.schematic")
    val FORBIDDEN_CITY_ROOMS = arrayOf(
        ROOM_CAVE,
        ROOM_FARM,
        ROOM_HOTEL,
        ROOM_MEETING_HALL,
        ROOM_MUSHROOM,
        ROOM_RUIN,
        ROOM_TANKS
    )

    ///
    /// Desert oasis structure related schematics
    ///

    val DESERT_OASIS = load("schematics/desert_oasis/oasis.schematic")

    val TOWER_OPEN = load("schematics/desert_oasis/large_structures/tower_open.schematic")
    val TOWER_SPACE_NEEDLE = load("schematics/desert_oasis/large_structures/tower_space_needle.schematic")

    val DESERT_OASIS_LARGE_PLOTS = arrayOf(
        TOWER_OPEN,
        TOWER_SPACE_NEEDLE
    )

    val ARCH_HOUSE = load("schematics/desert_oasis/small_structures/arch_house.schematic")
    val CIRCLE_OF_POWER = load("schematics/desert_oasis/small_structures/circle_of_power.schematic")
    val DEAD_FARM = load("schematics/desert_oasis/small_structures/dead_farm.schematic")
    val FLOWER_SHOP = load("schematics/desert_oasis/small_structures/flower_shop.schematic")
    val GLASS_PIT = load("schematics/desert_oasis/small_structures/glass_pit.schematic")
    val NOT_A_BROTHEL = load("schematics/desert_oasis/small_structures/not_a_brothel.schematic")
    val OVAL_HUT = load("schematics/desert_oasis/small_structures/oval_hut.schematic")
    val STONE_TOWER = load("schematics/desert_oasis/small_structures/stone_tower.schematic")
    val T_HOUSE = load("schematics/desert_oasis/small_structures/t_house.schematic")
    val TRADE_POST = load("schematics/desert_oasis/small_structures/trade_post.schematic")
    val WOOD_TOWER = load("schematics/desert_oasis/small_structures/wood_tower.schematic")

    val DESERT_OASIS_SMALL_PLOTS = arrayOf(
        ARCH_HOUSE,
        CIRCLE_OF_POWER,
        DEAD_FARM,
        FLOWER_SHOP,
        GLASS_PIT,
        NOT_A_BROTHEL,
        OVAL_HUT,
        STONE_TOWER,
        T_HOUSE,
        TRADE_POST,
        WOOD_TOWER
    )

    val ARCH = load("schematics/desert_oasis/medium_structures/arch.schematic")
    val BIG_HOUSE = load("schematics/desert_oasis/medium_structures/big_house.schematic")
    val FORGE = load("schematics/desert_oasis/medium_structures/forge.schematic")
    val SACRED_MANGROVE = load("schematics/desert_oasis/medium_structures/sacred_mangrove.schematic")
    val SAND_PIT = load("schematics/desert_oasis/medium_structures/sand_pit.schematic")
    val SPRING = load("schematics/desert_oasis/medium_structures/spring.schematic")
    val STABLES = load("schematics/desert_oasis/medium_structures/stables.schematic")

    val DESERT_OASIS_MEDIUM_PLOTS = arrayOf(
        ARCH,
        BIG_HOUSE,
        FORGE,
        SACRED_MANGROVE,
        SAND_PIT,
        SPRING,
        STABLES
    )

    // A list of all schematics present in AOTD
    val LIST = arrayOf(
        CRYPT,
        WITCH_HUT,
        VOID_CHEST_PORTAL,
        VOID_CHEST,
        NIGHTMARE_ISLAND,
        ENARIAS_ALTAR,
        OBSERVATORY,
        BED_HOUSE,
        ENARIA_LAIR,
        STAIRWELL,
        CONNECTOR,
        *DARK_FOREST_TREES,
        *DARK_FOREST_PROPS,
        *FORBIDDEN_CITY_ROOMS,
        ROOM_STAIR_DOWN,
        ROOM_STAIR_UP,
        *DESERT_OASIS_LARGE_PLOTS,
        *DESERT_OASIS_SMALL_PLOTS,
        *DESERT_OASIS_MEDIUM_PLOTS,
        DESERT_OASIS,
        ALTAR_RUINS
    )

    val NAME_TO_SCHEMATIC = LIST.map { it.getName() to it }.toMap()

    /**
     * Creates a schematic from the file path
     *
     * @param path The path to load from
     * @return The schematic in memory
     */
    private fun load(path: String): Schematic {
        return SchematicBuilder()
            .withFile(ResourceLocation(Constants.MOD_ID, path))
            .withCacheEnabled(ModCommonConfiguration.cacheStructures)
            .build()
    }
}