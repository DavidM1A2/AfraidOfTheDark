package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

import java.util.Set;

/**
 * Void chest structure class
 */
public class StructureVoidChest extends AOTDStructure
{
    // A set of compatible biomes
    private final Set<Biome> COMPATIBLE_BIOMES = ImmutableSet.of(
            Biomes.COLD_BEACH,
            Biomes.COLD_TAIGA,
            Biomes.COLD_TAIGA_HILLS,
            Biomes.MUTATED_TAIGA_COLD,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.ICE_PLAINS,
            Biomes.ICE_MOUNTAINS,
            Biomes.MUTATED_ICE_FLATS
    );

    /**
     * Structure constructor just sets the registry name
     */
    public StructureVoidChest()
    {
        super("void_chest");
    }

    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return A value between 0 and 1 which is the chance between 0% and 100% that a structure could spawn at the given position
     */
    @Override
    public double computeChanceToGenerateAt(BlockPos blockPos, IHeightmap heightmap, BiomeProvider biomeProvider)
    {
        return this.processChunks(new IChunkProcessor<Double>()
        {
            // Compute the minimum and maximum height over all the chunks that the void chest will cross over
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;

            @Override
            public boolean processChunk(ChunkPos chunkPos)
            {
                Set<Biome> biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z);
                // Void Chests only spawn in snowy biomes
                if (biomes.stream().noneMatch(COMPATIBLE_BIOMES::contains))
                {
                    return false;
                }

                // Compute min and max height
                minHeight = Math.min(minHeight, heightmap.getLowestHeight(chunkPos));
                maxHeight = Math.max(maxHeight, heightmap.getHighestHeight(chunkPos));
                return true;
            }

            @Override
            public Double getResult()
            {
                // If there's more than 8 blocks between the top and bottom block it's an invalid place for a void chest because it's not 'flat' enough
                if ((maxHeight - minHeight) > 8)
                {
                    return this.getDefaultResult();
                }

                // 5% chance to generate in any chunks this fits in
                return 0.05;
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
     * @param data     NBT containing the void chest's position
     */
    @Override
    public void generate(World world, ChunkPos chunkPos, NBTTagCompound data)
    {
        // Get the void chest's position from the NBT data
        BlockPos blockPos = this.getPosition(data);
        // This structure is simple, it is just the void chest schematic
        SchematicGenerator.generateSchematic(ModSchematics.VOID_CHEST, world, blockPos, chunkPos, ModLootTables.VOID_CHEST);
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

        // Find the lowest y value containing a block
        int groundLevel = this.processChunks(new LowestHeightChunkProcessor(OverworldHeightmap.get(world)), new InteriorChunkIterator(this, blockPos));
        // Set the schematic's position to the lowest point in the chunk
        blockPos = new BlockPos(blockPos.getX(), groundLevel - 7, blockPos.getZ());
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
        return ModSchematics.VOID_CHEST.getWidth();
    }

    /**
     * @return The length of the structure in blocks
     */
    @Override
    public int getZLength()
    {
        return ModSchematics.VOID_CHEST.getLength();
    }
}
