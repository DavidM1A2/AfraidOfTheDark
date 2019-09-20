package com.davidm1a2.afraidofthedark.common.constants;

import com.davidm1a2.afraidofthedark.common.block.*;
import com.davidm1a2.afraidofthedark.common.block.gravewood.*;
import com.davidm1a2.afraidofthedark.common.block.mangrove.*;
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityDarkForest;
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityEnariaSpawner;
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityGhastlyEnariaSpawner;
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A static class containing all of our block references for us
 */
public class ModBlocks
{
    public static final Block GRAVEWOOD = new BlockGravewood();
    public static final Block GRAVEWOOD_PLANKS = new BlockGravewoodPlanks();
    public static final Block GRAVEWOOD_LEAVES = new BlockGravewoodLeaves();
    public static final Block GRAVEWOOD_HALF_SLAB = new BlockGravewoodHalfSlab();
    public static final Block GRAVEWOOD_DOUBLE_SLAB = new BlockGravewoodDoubleSlab();
    public static final Block GRAVEWOOD_STAIRS = new BlockGravewoodStairs();
    public static final Block GRAVEWOOD_SAPLING = new BlockGravewoodSapling();

    public static final Block MANGROVE = new BlockMangrove();
    public static final Block MANGROVE_PLANKS = new BlockMangrovePlanks();
    public static final Block MANGROVE_LEAVES = new BlockMangroveLeaves();
    public static final Block MANGROVE_HALF_SLAB = new BlockMangroveHalfSlab();
    public static final Block MANGROVE_DOUBLE_SLAB = new BlockMangroveDoubleSlab();
    public static final Block MANGROVE_STAIRS = new BlockMangroveStairs();
    public static final Block MANGROVE_SAPLING = new BlockMangroveSapling();

    public static final Block METEOR = new BlockMeteor();
    public static final Block ASTRAL_SILVER_ORE = new BlockAstralSilverOre();
    public static final Block SUNSTONE_ORE = new BlockSunstoneOre();
    public static final Block STAR_METAL_ORE = new BlockStarMetalOre();
    public static final Block IGNEOUS = new BlockIgneous();

    public static final Block GNOMISH_METAL_STRUT = new BlockGnomishMetalStrut();
    public static final Block GNOMISH_METAL_PLATE = new BlockGnomishMetalPlate();
    public static final Block GLOW_STALK = new BlockGlowStalk();

    public static final Block ELDRITCH_STONE = new BlockEldritchStone();
    public static final Block ELDRITCH_OBSIDIAN = new BlockEldritchObsidian();
    public static final Block AMORPHOUS_ELDRITCH_METAL = new BlockAmorphousEldritchMetal();
    public static final Block VOID_CHEST = new BlockVoidChest();
    public static final Block VOID_CHEST_PORTAL = new BlockVoidChestPortal();
    public static final Block DARK_FOREST = new BlockDarkForest();
    public static final Block ENARIA_SPAWNER = new BlockEnariaSpawner();
    public static final Block ENARIAS_ALTAR = new BlockEnariasAltar();

    // An array containing a list of blocks that AOTD adds
    public static final Block[] BLOCK_LIST = new Block[]
            {
                    GRAVEWOOD,
                    GRAVEWOOD_PLANKS,
                    GRAVEWOOD_SAPLING,
                    GRAVEWOOD_LEAVES,
                    GRAVEWOOD_HALF_SLAB,
                    GRAVEWOOD_DOUBLE_SLAB,
                    GRAVEWOOD_STAIRS,
                    MANGROVE,
                    MANGROVE_PLANKS,
                    MANGROVE_SAPLING,
                    MANGROVE_LEAVES,
                    MANGROVE_HALF_SLAB,
                    MANGROVE_DOUBLE_SLAB,
                    MANGROVE_STAIRS,
                    METEOR,
                    ASTRAL_SILVER_ORE,
                    ELDRITCH_STONE,
                    ELDRITCH_OBSIDIAN,
                    AMORPHOUS_ELDRITCH_METAL,
                    VOID_CHEST,
                    VOID_CHEST_PORTAL,
                    DARK_FOREST,
                    ENARIA_SPAWNER,
                    SUNSTONE_ORE,
                    STAR_METAL_ORE,
                    IGNEOUS,
                    GNOMISH_METAL_STRUT,
                    GNOMISH_METAL_PLATE,
                    GLOW_STALK,
                    ENARIAS_ALTAR
            };

    // A list of tile entities to register
    public static final Pair<Class<? extends TileEntity>, ResourceLocation>[] TILE_ENTITY_LIST = ArrayUtils.toArray(
            Pair.of(TileEntityVoidChest.class, new ResourceLocation(Constants.MOD_ID, "tile_entity_void_chest")),
            Pair.of(TileEntityDarkForest.class, new ResourceLocation(Constants.MOD_ID, "tile_entity_dark_forest")),
            Pair.of(TileEntityGhastlyEnariaSpawner.class, new ResourceLocation(Constants.MOD_ID, "tile_entity_ghastly_enaria_spawner")),
            Pair.of(TileEntityEnariaSpawner.class, new ResourceLocation(Constants.MOD_ID, "tile_entity_enaria_spawner"))
    );
}
