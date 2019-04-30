package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
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
	 * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
	 * @return A value between 0 and 1 which is the chance between 0% and 100% that a structure could spawn at the given position
	 */
	public abstract double computeChanceToGenerateAt(BlockPos blockPos, IHeightmap heightmap, BiomeProvider biomeProvider);

	/**
	 * Generates the structure at a position with an optional argument of chunk position
	 *  @param world The world to generate the structure in
	 * @param blockPos The position to generate the structure at
	 * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
	 * @param data Any additional structure data that is needed for generation
	 */
	public abstract void generate(World world, BlockPos blockPos, ChunkPos chunkPos, NBTTagCompound data);

	/**
	 * Called to generate a random permutation of the structure. This is useful when the structure requires
	 * random parameters to be set before starting generation.
	 *
	 * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate. Default returns an empty NBT
	 */
	public NBTTagCompound generateStructureData()
	{
		return new NBTTagCompound();
	}

	/**
	 * @return The width of the structure in blocks
	 */
	public abstract int getXWidth();

	/**
	 * @return The length of the structure in blocks
	 */
	public abstract int getZLength();
}
