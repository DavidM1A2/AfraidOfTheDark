package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.block.BlockAstralSilverOre;
import com.DavidM1A2.afraidofthedark.common.block.BlockMeteor;
import com.DavidM1A2.afraidofthedark.common.block.gravewood.*;
import net.minecraft.block.Block;

/**
 * A static class containing all of our block references for us
 */
public class ModBlocks
{
	public static final Block GRAVEWOOD = new BlockGravewood();
	public static final Block GRAVEWOOD_PLANKS = new BlockGravewoodPlanks();
	public static final Block GRAVEWOOD_SAPLING = new BlockGravewoodSapling();
	public static final Block GRAVEWOOD_LEAVES = new BlockGravewoodLeaves();
	public static final Block GRAVEWOOD_HALF_SLAB = new BlockGravewoodHalfSlab();
	public static final Block GRAVEWOOD_DOUBLE_SLAB = new BlockGravewoodDoubleSlab();
	public static final Block GRAVEWOOD_STAIRS = new BlockGravewoodStairs();

	public static final Block METEOR = new BlockMeteor();
	public static final Block ASTRAL_SILVER_ORE = new BlockAstralSilverOre();

	// An array containing a list of blocks that AOTD adds
	public static Block[] BLOCK_LIST = new Block[]
	{
		GRAVEWOOD,
		GRAVEWOOD_PLANKS,
		GRAVEWOOD_SAPLING,
		GRAVEWOOD_LEAVES,
		GRAVEWOOD_HALF_SLAB,
		GRAVEWOOD_DOUBLE_SLAB,
		GRAVEWOOD_STAIRS,
		METEOR,
		ASTRAL_SILVER_ORE
	};
}
