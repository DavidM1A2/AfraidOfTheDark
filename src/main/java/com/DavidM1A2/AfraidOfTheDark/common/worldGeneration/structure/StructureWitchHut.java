package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

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
		return this.processChunks(new IChunkProcessor<Double>()
		{
			// Compute the minimum and maximum height over all the chunks that the witch hut will cross over
			int minHeight = Integer.MAX_VALUE;
			int maxHeight = Integer.MIN_VALUE;

			@Override
			public boolean processChunk(ChunkPos chunkPos)
			{
				Set<Biome> biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z);
				// Witch huts can only spawn in erie forests
				if (!biomes.contains(ModBiomes.ERIE_FOREST) || biomes.size() > 1)
					return false;

				// Compute min and max height
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
		}, new InteriorChunkIterator(this, blockPos));
	}

	/**
	 * Generates the structure at a position with an optional argument of chunk position
	 *  @param world The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data NBT data containing the structure's position
     */
	@Override
	public void generate(World world, ChunkPos chunkPos, NBTTagCompound data)
	{
		// Grab the block pos from the NBT data
		BlockPos blockPos = this.getPosition(data);
		// This structure is simple, it is just the witch hut schematic
		SchematicGenerator.generateSchematic(ModSchematics.WITCH_HUT, world, blockPos, chunkPos, ModLootTables.WITCH_HUT);
	}

	/**
	 * Called to generate a random permutation of the structure. Set the structure's position
	 *
	 * @param world The world to generate the structure's data for
	 * @param blockPos The position's x and z coordinates to generate the structure at
	 * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
	 */
	@Override
	public NBTTagCompound generateStructureData(World world, BlockPos blockPos, BiomeProvider biomeProvider)
	{
		NBTTagCompound compound = new NBTTagCompound();

		// Find the lowest y value containing a block
		int groundLevel = this.processChunks(new LowestHeightChunkProcessor(OverworldHeightmap.get(world)), new InteriorChunkIterator(this, blockPos));
		// Set the schematic at the lowest point in the chunk
		blockPos = new BlockPos(blockPos.getX(), groundLevel - 1, blockPos.getZ());
		// Update the NBT
		compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos));

		return compound;
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
