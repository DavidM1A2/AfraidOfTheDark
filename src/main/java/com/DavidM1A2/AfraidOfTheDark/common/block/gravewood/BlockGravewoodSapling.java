package com.DavidM1A2.afraidofthedark.common.block.gravewood;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDSapling;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

/**
 * Block representing a gravewood sapling
 */
public class BlockGravewoodSapling extends AOTDSapling
{
	/**
	 * Constructor initializes the sapling with a name
	 */
	public BlockGravewoodSapling()
	{
		super("gravewood_sapling");
	}

	/**
	 * Causes the tree to grow. Uses the default MC tree generation algorithm
	 *
	 * @param world The world the sapling is growing in
	 * @param pos The position of the sapling
	 * @param state The current state of the sapling
	 * @param random The random object to grow the tree with
	 */
	@Override
	public void causeTreeToGrow(World world, BlockPos pos, IBlockState state, Random random)
	{
		// Make sure we have room for a tree
		if (!TerrainGen.saplingGrowTree(world, random, pos))
			return;

		// Create a tree generator based on the gavewood log and leaf types
		WorldGenerator treeGenerator = new WorldGenTrees(true, 6, ModBlocks.GRAVEWOOD.getDefaultState(), ModBlocks.GRAVEWOOD_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false), true);

		// Remove the sapling block
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

		// Attempt to generate the tree, if it fails, set the base block back to its original state
		if (!treeGenerator.generate(world, random, pos))
			world.setBlockState(pos, state, 4);
	}
}
