package com.davidm1a2.afraidofthedark.common.constants;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.Schematic;
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicBuilder;
import net.minecraft.util.ResourceLocation;

import java.util.stream.Stream;

/**
 * A static class containing all of our schematic references for us
 */
public class ModSchematics
{
    ///
    /// Standalone schematics
    ///

    public static final Schematic CRYPT = load("schematics/crypt.schematic");
    public static final Schematic WITCH_HUT = load("schematics/witch_hut.schematic");
    public static final Schematic VOID_CHEST_PORTAL = load("schematics/void_chest_portal.schematic");
    public static final Schematic VOID_CHEST = load("schematics/void_chest.schematic");
    public static final Schematic NIGHTMARE_ISLAND = load("schematics/nightmare_island.schematic");
    public static final Schematic ENARIAS_ALTAR = load("schematics/enarias_altar.schematic");

    ///
    /// Dark Forest structure related schematics
    ///

    public static final Schematic BED_HOUSE = load("schematics/dark_forest/bed_house.schematic");

    public static final Schematic TREE_BRANCHY_TYPE_1 = load("schematics/dark_forest/tree_branchy_type_1.schematic");
    public static final Schematic TREE_BRANCHY_TYPE_2 = load("schematics/dark_forest/tree_branchy_type_2.schematic");
    public static final Schematic TREE_LARGE_CIRCLE = load("schematics/dark_forest/tree_large_circle.schematic");
    public static final Schematic TREE_LARGE_DONUT = load("schematics/dark_forest/tree_large_donut.schematic");
    public static final Schematic TREE_SMALL = load("schematics/dark_forest/tree_small.schematic");
    public static final Schematic[] DARK_FOREST_TREES = {TREE_BRANCHY_TYPE_1, TREE_BRANCHY_TYPE_2, TREE_LARGE_CIRCLE, TREE_LARGE_DONUT, TREE_SMALL};

    public static final Schematic PROP_BUSH_1 = load("schematics/dark_forest/prop_bush_1.schematic");
    public static final Schematic PROP_FALLEN_OVER_LOG = load("schematics/dark_forest/prop_fallen_over_log.schematic");
    public static final Schematic PROP_FENCE_1 = load("schematics/dark_forest/prop_fence_1.schematic");
    public static final Schematic PROP_FENCE_2 = load("schematics/dark_forest/prop_fence_2.schematic");
    public static final Schematic PROP_FOUNTAIN = load("schematics/dark_forest/prop_fountain.schematic");
    public static final Schematic PROP_LOG = load("schematics/dark_forest/prop_log.schematic");
    public static final Schematic PROP_PUMPKIN_1 = load("schematics/dark_forest/prop_pumpkin_1.schematic");
    public static final Schematic PROP_PUMPKIN_2 = load("schematics/dark_forest/prop_pumpkin_2.schematic");
    public static final Schematic PROP_STUMP = load("schematics/dark_forest/prop_stump.schematic");
    public static final Schematic[] DARK_FOREST_PROPS = {PROP_BUSH_1, PROP_FALLEN_OVER_LOG, PROP_FENCE_1, PROP_FENCE_2, PROP_FOUNTAIN, PROP_LOG, PROP_PUMPKIN_1, PROP_PUMPKIN_2, PROP_STUMP};

    ///
    /// Gnomish city structure related schematics
    ///

    public static final Schematic ENARIA_LAIR = load("schematics/gnomish_city/enaria_lair.schematic");

    public static final Schematic STAIRWELL = load("schematics/gnomish_city/stairwell.schematic");
    public static final Schematic TUNNEL_EW = load("schematics/gnomish_city/tunnel_ew.schematic");
    public static final Schematic TUNNEL_NS = load("schematics/gnomish_city/tunnel_ns.schematic");

    public static final Schematic ROOM_CAVE = load("schematics/gnomish_city/room_cave.schematic");
    public static final Schematic ROOM_FARM = load("schematics/gnomish_city/room_farm.schematic");
    public static final Schematic ROOM_HOTEL = load("schematics/gnomish_city/room_hotel.schematic");
    public static final Schematic ROOM_MEETING_HALL = load("schematics/gnomish_city/room_meeting_hall.schematic");
    public static final Schematic ROOM_MUSHROOM = load("schematics/gnomish_city/room_mushroom.schematic");
    public static final Schematic ROOM_RUIN = load("schematics/gnomish_city/room_ruin.schematic");
    public static final Schematic ROOM_STAIR_DOWN = load("schematics/gnomish_city/room_stair_down.schematic");
    public static final Schematic ROOM_STAIR_UP = load("schematics/gnomish_city/room_stair_up.schematic");
    public static final Schematic ROOM_TANKS = load("schematics/gnomish_city/room_tanks.schematic");
    public static final Schematic[] GNOMISH_CITY_ROOMS = {ROOM_CAVE, ROOM_FARM, ROOM_HOTEL, ROOM_MEETING_HALL, ROOM_MUSHROOM, ROOM_RUIN, ROOM_TANKS};

    // A list of all schematics present in AOTD
    public static final Schematic[] LIST =
            Stream.of(
                    // Create an array of standalone schematics
                    new Schematic[]
                            {
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
                                    TUNNEL_NS
                            },
                    // Join it with arrays of sub-component schematics
                    DARK_FOREST_TREES,
                    DARK_FOREST_PROPS,
                    GNOMISH_CITY_ROOMS)
                    // Join the arrays
                    .flatMap(Stream::of)
                    .toArray(Schematic[]::new);

    /**
     * Creates a schematic from the file path
     *
     * @param path The path to load from
     * @return The schematic in memory
     */
    private static Schematic load(String path)
    {
        return new SchematicBuilder().withFile(new ResourceLocation(Constants.MOD_ID, path)).withCacheEnabled(AfraidOfTheDark.INSTANCE.getConfigurationHandler().getCacheStructures()).build();
    }
}
