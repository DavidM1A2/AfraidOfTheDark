package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;

/**
 * Class representing the nightmare island structure, it will not generate naturally
 */
public class StructureNightmareIsland extends AOTDStructure
{
    /**
     * Constructor sets the structure name
     */
    public StructureNightmareIsland()
    {
        super("nightmare_island");
    }

    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos The position that the structure would begin at
     * @param heightmap The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return 0, this structure does not generate
     */
    @Override
    public double computeChanceToGenerateAt(BlockPos blockPos, IHeightmap heightmap, BiomeProvider biomeProvider)
    {
        return 0;
    }

    /**
     * Generates the structure with an optional argument of chunk position
     *
     * @param world The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data Any additional structure data that is needed for generation such as structure position
     */
    @Override
    public void generate(World world, ChunkPos chunkPos, NBTTagCompound data)
    {
        // Get the position of the structure from the data compound
        BlockPos blockPos = this.getPosition(data);
        // This structure is simple, it is just the crypt schematic
        SchematicGenerator.generateSchematic(ModSchematics.NIGHTMARE_ISLAND, world, blockPos, chunkPos, ModLootTables.NIGHTMARE_ISLAND);
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

        // Set the position to the blockpos
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos));

        return compound;
    }

    /**
     * @return The width of the structure in blocks
     */
    @Override
    public int getXWidth()
    {
        return ModSchematics.NIGHTMARE_ISLAND.getWidth();
    }

    /**
     * @return The length of the structure in blocks
     */
    @Override
    public int getZLength()
    {
        return ModSchematics.NIGHTMARE_ISLAND.getLength();
    }
}
