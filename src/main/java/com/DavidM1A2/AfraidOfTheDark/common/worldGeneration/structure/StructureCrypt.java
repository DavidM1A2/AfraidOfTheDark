package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.IChunkProcessor;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

import java.util.Set;

/**
 * Crypt structure class
 */
public class StructureCrypt extends AOTDStructure
{
    // A set of incompatible biomes
    private final Set<Biome> INCOMPATIBLE_BIOMES = ImmutableSet.of(
            Biomes.OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.FROZEN_OCEAN,
            Biomes.BEACH,
            Biomes.FROZEN_RIVER,
            Biomes.RIVER,
            Biomes.SKY,
            Biomes.VOID,
            Biomes.STONE_BEACH
    );

    /**
     * Constructor just initializes the name
     */
    public StructureCrypt()
    {
        super("crypt");
    }

    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return true if the structure fits at the position, false otherwise
     */
    @Override
    public double computeChanceToGenerateAt(BlockPos blockPos, IHeightmap heightmap, BiomeProvider biomeProvider)
    {
        return this.processInteriorChunks(new IChunkProcessor<Double>()
        {
            // Compute the minimum and maximum height over all the chunks that the crypt will cross over
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;

            // Counters for the number of erie forest chunks
            int numErieForestChunks = 0;
            int numOtherChunks = 0;

            @Override
            public boolean processChunk(int chunkX, int chunkZ)
            {
                Set<Biome> biomes = approximateBiomesInChunk(biomeProvider, chunkX, chunkZ);
                // Filter incompatible biomes
                if (biomes.stream().anyMatch(INCOMPATIBLE_BIOMES::contains))
                    return false;
                // If the biome is an erie forest then increment erie forest
                else if (biomes.contains(ModBiomes.ERIE_FOREST))
                    numErieForestChunks++;
                // It's a different biome
                else
                    numOtherChunks++;

                // Compute min and max height
                ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
                minHeight = Math.min(minHeight, heightmap.getLowestHeight(chunkPos));
                maxHeight = Math.max(maxHeight, heightmap.getHighestHeight(chunkPos));

                return true;
            }

            @Override
            public Double getResult()
            {
                // If there's more than 5 blocks between the top and bottom block it's an invalid place for a crypt because it's not 'flat' enough
                if ((maxHeight - minHeight) > 5)
                    return this.getDefaultResult();

                // Compute how many chunks are erie forest and how many are other biomes
                double percentErie = (double) numErieForestChunks / (numErieForestChunks + numOtherChunks);
                double percentOther = 1.0 - percentErie;

                // 10% chance to spawn in other biomes, 80% chance to spawn in erie forests
                return percentErie * 0.8 + percentOther * 0.1;
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
     *  @param world    The world to generate the structure in
     * @param blockPos The position to generate the structure at
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data ignored
     */
    @Override
    public void generate(World world, BlockPos blockPos, ChunkPos chunkPos, NBTTagCompound data)
    {
        // Grab the world's heightmap
        IHeightmap heightmap = OverworldHeightmap.get(world);
        // Make sure the heightmap is not null
        if (heightmap != null)
        {
            // Compute the center block of the schematic
            BlockPos centerBlock = blockPos.add(this.getXWidth() / 2, 0, this.getZLength() / 2);
            // Convert that block to the chunk it is in
            ChunkPos centerChunk = new ChunkPos(centerBlock);
            // Compute the ground height at the center
            int groundHeight = heightmap.getLowestHeight(centerChunk);

            // Set the schematic height to be underground + 5 blocks
            BlockPos schematicPos = new BlockPos(blockPos.getX(), groundHeight - ModSchematics.CRYPT.getHeight() + 3, blockPos.getZ());
            // This structure is simple, it is just the crypt schematic
            SchematicGenerator.generateSchematic(ModSchematics.CRYPT, world, schematicPos, chunkPos, ModLootTables.CRYPT);
        }
    }

    /**
     * @return The width of the structure in blocks
     */
    @Override
    public int getXWidth()
    {
        // For this structure the width is just the width of the crypt
        return ModSchematics.CRYPT.getWidth();
    }

    /**
     * @return The length of the structure in blocks
     */
    @Override
    public int getZLength()
    {
        // For this structure the length is just the length of the crypt
        return ModSchematics.CRYPT.getLength();
    }
}
