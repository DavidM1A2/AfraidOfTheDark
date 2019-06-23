package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.Schematic;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicLoader;
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

    public static final Schematic CRYPT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/crypt.schematic"));
    public static final Schematic WITCH_HUT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/witch_hut.schematic"));
    public static final Schematic VOID_CHEST_PORTAL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/void_chest_portal.schematic"));
    public static final Schematic VOID_CHEST = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/void_chest.schematic"));
    public static final Schematic NIGHTMARE_ISLAND = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/nightmare_island.schematic"));
    public static final Schematic ENARIAS_ALTAR = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/enarias_altar.schematic"));

    ///
    /// Dark Forest structure related schematics
    ///

    public static final Schematic BED_HOUSE = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/bed_house.schematic"));

    public static final Schematic TREE_BRANCHY_TYPE_1 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/tree_branchy_type_1.schematic"));
    public static final Schematic TREE_BRANCHY_TYPE_2 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/tree_branchy_type_2.schematic"));
    public static final Schematic TREE_LARGE_CIRCLE = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/tree_large_circle.schematic"));
    public static final Schematic TREE_LARGE_DONUT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/tree_large_donut.schematic"));
    public static final Schematic TREE_SMALL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/tree_small.schematic"));
    public static final Schematic[] DARK_FOREST_TREES = {TREE_BRANCHY_TYPE_1, TREE_BRANCHY_TYPE_2, TREE_LARGE_CIRCLE, TREE_LARGE_DONUT, TREE_SMALL};

    public static final Schematic PROP_BUSH_1 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_bush_1.schematic"));
    public static final Schematic PROP_FALLEN_OVER_LOG = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_fallen_over_log.schematic"));
    public static final Schematic PROP_FENCE_1 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_fence_1.schematic"));
    public static final Schematic PROP_FENCE_2 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_fence_2.schematic"));
    public static final Schematic PROP_FOUNTAIN = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_fountain.schematic"));
    public static final Schematic PROP_LOG = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_log.schematic"));
    public static final Schematic PROP_PUMPKIN_1 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_pumpkin_1.schematic"));
    public static final Schematic PROP_PUMPKIN_2 = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_pumpkin_2.schematic"));
    public static final Schematic PROP_STUMP = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/dark_forest/prop_stump.schematic"));
    public static final Schematic[] DARK_FOREST_PROPS = {PROP_BUSH_1, PROP_FALLEN_OVER_LOG, PROP_FENCE_1, PROP_FENCE_2, PROP_FOUNTAIN, PROP_LOG, PROP_PUMPKIN_1, PROP_PUMPKIN_2, PROP_STUMP};

    ///
    /// Gnomish city structure related schematics
    ///

    public static final Schematic ENARIA_LAIR = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/enaria_lair.schematic"));

    public static final Schematic STAIRWELL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/stairwell.schematic"));
    public static final Schematic TUNNEL_EW = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/tunnel_ew.schematic"));
    public static final Schematic TUNNEL_NS = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/tunnel_ns.schematic"));

    public static final Schematic ROOM_CAVE = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_cave.schematic"));
    public static final Schematic ROOM_FARM = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_farm.schematic"));
    public static final Schematic ROOM_HOTEL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_hotel.schematic"));
    public static final Schematic ROOM_MEETING_HALL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_meeting_hall.schematic"));
    public static final Schematic ROOM_MUSHROOM = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_mushroom.schematic"));
    public static final Schematic ROOM_RUIN = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_ruin.schematic"));
    public static final Schematic ROOM_STAIR_DOWN = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_stair_down.schematic"));
    public static final Schematic ROOM_STAIR_UP = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_stair_up.schematic"));
    public static final Schematic ROOM_TANKS = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/gnomish_city/room_tanks.schematic"));
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
}
