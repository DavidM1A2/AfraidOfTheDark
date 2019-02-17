package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.common.biomes.BiomeErieForest;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.IChunkProcessor;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.LowestHeightChunkProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeRiver;

import java.util.Set;

/**
 * Witch hut structure class
 */
public class StructureWitchHut extends AOTDStructure
{
	/**
	 * Structure constructor just sets the registry name
	 */
	public StructureWitchHut()
	{
		super("witch_hut");
	}

	/**
	 * Tests if this structure is valid for the given position
	 *
	 * @param blockPos The position that the structure would begin at
	 * @param heightmap The heightmap to use in deciding if the structure will fit at the position
	 * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
	 * @return true if the structure fits at the position, false otherwise
	 */
	@Override
	public double computeChanceToGenerateAt(BlockPos blockPos, IHeightmap heightmap, BiomeProvider biomeProvider)
	{
		return this.processInteriorChunks(new IChunkProcessor<Double>()
		{
			// Compute the minimum and maximum height over all the chunks that the witch hut will cross over
			int minHeight = Integer.MAX_VALUE;
			int maxHeight = Integer.MIN_VALUE;

			@Override
			public boolean processChunk(int chunkX, int chunkZ)
			{
				Set<Biome> biomes = approximateBiomesInChunk(biomeProvider, chunkX, chunkZ);
				// Witch huts can only spawn in erie forests
				if (!biomes.contains(ModBiomes.ERIE_FOREST) || biomes.size() > 1)
					return false;

				// Compute min and max height
				ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
				minHeight = Math.min(minHeight, heightmap.getLowestHeight(chunkPos));
				maxHeight = Math.max(maxHeight, heightmap.getHighestHeight(chunkPos));
				return true;
			}

			@Override
			public Double getResult()
			{
				// If there's more than 3 blocks between the top and bottom block it's an invalid place for a witch hut because it's not 'flat' enough
				if ((maxHeight - minHeight) > 3)
					return this.getDefaultResult();

				// 30% chance to generate in any chunks this fits in
				return 0.3;
			}

			@Override
			public Double getDefaultResult()
			{
				return 0D;
			}
		}, blockPos);
	}

	/**
	 * Generates the structure at a position with an optional argument of chunk position
	 *
	 * @param world The world to generate the structure in
	 * @param blockPos The position to generate the structure at
	 * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
	 */
	@Override
	public void generate(World world, BlockPos blockPos, ChunkPos chunkPos)
	{
		// Grab the world's heightmap
		IHeightmap heightmap = OverworldHeightmap.get(world);
		// Make sure the heightmap is not null
		if (heightmap != null)
		{
			int minGroundHeight = this.processInteriorChunks(new LowestHeightChunkProcessor(heightmap), blockPos);
			// Set the schematic at the lowest point in the chunk
			BlockPos schematicPos = new BlockPos(blockPos.getX(), minGroundHeight - 1, blockPos.getZ());
			// This structure is simple, it is just the witch hut schematic
			SchematicGenerator.generateSchematic(ModSchematics.WITCH_HUT, world, schematicPos, chunkPos, ModLootTables.WITCH_HUT);
		}
	}

	/**
	 * @return The width of the structure in blocks
	 */
	@Override
	public int getXWidth()
	{
		// For this structure the width is just the width of the witch hut
		return ModSchematics.WITCH_HUT.getWidth();
	}

	/**
	 * @return The length of the structure in blocks
	 */
	@Override
	public int getZLength()
	{
		// For this structure the length is just the length of the witch hut
		return ModSchematics.WITCH_HUT.getLength();
	}
}
