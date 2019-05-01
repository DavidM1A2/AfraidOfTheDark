package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.Schematic;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicLoader;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our schematic references for us
 */
public class ModSchematics
{
    public static final Schematic CRYPT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/crypt.schematic"));

    public static final Schematic WITCH_HUT = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/witch_hut.schematic"));

    public static final Schematic VOID_CHEST_PORTAL = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/void_chest_portal.schematic"));

    public static final Schematic VOID_CHEST = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, "schematics/void_chest.schematic"));

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
}
