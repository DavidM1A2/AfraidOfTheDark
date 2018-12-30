package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Base class for all structures in the game that are generated using schematics
 */
public abstract class Structure extends IForgeRegistryEntry.Impl<Structure>
{
	/**
	 * Tests if this structure is valid for the given position
	 *
	 * @param blockPos The position that the structure would begin at
	 * @param heightmap The heightmap to use in deciding if the structure will fit at the position
	 * @return true if the structure fits at the position, false otherwise
	 */
	public abstract boolean canGenerateAt(BlockPos blockPos, IHeightmap heightmap);

	/**
	 * Generates the structure at a position with an optional argument of chunk position
	 *
	 * @param world The world to generate the structure in
	 * @param blockPos The position to generate the structure at
	 * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
	 */
	public abstract void generate(World world, BlockPos blockPos, ChunkPos chunkPos);

	/**
	 * @return The width of the structure in blocks
	 */
	public abstract int getXWidth();

	/**
	 * @return The length of the structure in blocks
	 */
	public abstract int getZLength();
}
