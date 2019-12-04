package com.davidm1a2.afraidofthedark.common.worldGeneration.structure;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes;
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables;
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics;
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator;
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
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
        return this.processChunks(new IChunkProcessor<Double>()
        {
            // Compute the minimum and maximum height over all the chunks that the crypt will cross over
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;

            // Counters for the number of erie forest chunks
            int numErieForestChunks = 0;
            int numOtherChunks = 0;

            @Override
            public boolean processChunk(ChunkPos chunkPos)
            {
                Set<Biome> biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z);
                // Filter incompatible biomes
                if (biomes.stream().anyMatch(INCOMPATIBLE_BIOMES::contains))
                {
                    return false;
                }
                // If the biome is an erie forest then increment erie forest
                else if (biomes.contains(ModBiomes.ERIE_FOREST))
                {
                    numErieForestChunks++;
                }
                // It's a different biome
                else
                {
                    numOtherChunks++;
                }

                // Compute min and max height
                minHeight = Math.min(minHeight, heightmap.getLowestHeight(chunkPos));
                maxHeight = Math.max(maxHeight, heightmap.getHighestHeight(chunkPos));

                return true;
            }

            @Override
            public Double getResult()
            {
                // If there's more than 5 blocks between the top and bottom block it's an invalid place for a crypt because it's not 'flat' enough
                if ((maxHeight - minHeight) > 5)
                {
                    return this.getDefaultResult();
                }

                // Compute how many chunks are erie forest and how many are other biomes
                double percentErie = (double) numErieForestChunks / (numErieForestChunks + numOtherChunks);
                double percentOther = 1.0 - percentErie;

                // 0.6% chance to spawn in other biomes, 2% chance to spawn in erie forests
                return (percentErie * 0.02 + percentOther * 0.006) * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getCryptMultiplier();
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
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     Data containing structure position
     */
    @Override
    public void generate(World world, ChunkPos chunkPos, NBTTagCompound data)
    {
        // Get the position of the structure from the data compound
        BlockPos blockPos = this.getPosition(data);
        // This structure is simple, it is just the crypt schematic
        SchematicGenerator.generateSchematic(ModSchematics.CRYPT, world, blockPos, chunkPos, ModLootTables.CRYPT);
    }

    /**
     * Called to generate a random permutation of the structure. Set the structure's position
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
     */
    @Override
    public NBTTagCompound generateStructureData(World world, BlockPos blockPos, BiomeProvider biomeProvider)
    {
        NBTTagCompound compound = new NBTTagCompound();

        // Compute the center block of the schematic
        BlockPos centerBlock = blockPos.add(this.getXWidth() / 2, 0, this.getZLength() / 2);
        // Convert that block to the chunk it is in
        ChunkPos centerChunk = new ChunkPos(centerBlock);
        // Compute the ground height at the center
        int groundHeight = OverworldHeightmap.Companion.get(world).getLowestHeight(centerChunk);

        // Set the schematic height to be underground + 3 blocks+, ensure it isn't below bedrock
        int y = MathHelper.clamp(groundHeight - ModSchematics.CRYPT.getHeight() + 3, 5, Integer.MAX_VALUE);
        blockPos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
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
