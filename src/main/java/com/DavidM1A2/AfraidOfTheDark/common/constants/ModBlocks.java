package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.block.gravewood.*;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	// An array containing a list of blocks that AOTD adds
	public static Block[] BLOCK_LIST = new Block[]
	{
		GRAVEWOOD,
		GRAVEWOOD_PLANKS,
		GRAVEWOOD_SAPLING,
		GRAVEWOOD_LEAVES,
		GRAVEWOOD_HALF_SLAB,
		GRAVEWOOD_DOUBLE_SLAB
	};
}
