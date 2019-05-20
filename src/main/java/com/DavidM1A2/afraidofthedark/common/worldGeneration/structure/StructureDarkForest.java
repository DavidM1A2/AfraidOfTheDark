package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.Schematic;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.RandomUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Dark forest structure class
 */
public class StructureDarkForest extends AOTDStructure
{
    // A set of compatible biomes
    private static final Set<Biome> COMPATIBLE_HOUSE_BIOMES = ImmutableSet.of(
            Biomes.SAVANNA,
            Biomes.MUTATED_SAVANNA,
            Biomes.PLAINS,
            Biomes.MUTATED_PLAINS,
            ModBiomes.ERIE_FOREST
    );
    // NBT tag keys
    private static final String NBT_TREES = "trees";
    private static final String NBT_PROPS = "props";
    private static final String NBT_HOUSE_POSITION = "house";
    private static final String NBT_SCHEMATIC_ID = "schematic_id";
    // A set of invalid prop biomes
    private final Set<Biome> INCOMPATIBLE_BIOMES = ImmutableSet.of(
            Biomes.OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.RIVER,
            Biomes.SKY,
            Biomes.VOID
    );
    // The width of the dark forest dungeon
    private int width;
    // The bed house width
    private int bedHouseWidth;
    // The height of the dark forest dungeon
    private int height;
    // The bed house length
    private int bedHouseLength;

    /**
     * Structure constructor just sets the registry name
     */
    public StructureDarkForest()
    {
        super("dark_forest");
        int widestTree = Arrays.stream(ModSchematics.DARK_FOREST_TREES).map(Schematic::getWidth).max(Short::compareTo).get();
        int longestTree = Arrays.stream(ModSchematics.DARK_FOREST_TREES).map(Schematic::getLength).max(Short::compareTo).get();
        this.bedHouseWidth = ModSchematics.BED_HOUSE.getWidth();
        this.bedHouseLength = ModSchematics.BED_HOUSE.getLength();
        // Width is width(BED_HOUSE) + 2*width(BIGGEST_TREE)
        this.width = this.bedHouseWidth + 2 * widestTree;
        // Height is length(BED_HOUSE) + 2*length(BIGGEST_TREE)
        this.height = this.bedHouseLength + 2 * longestTree;
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
        // Compute the two chunk position corners of the house
        BlockPos houseCorner1BlockPos = blockPos.add(this.getXWidth() / 2 - this.bedHouseWidth / 2, 0, this.getZLength() / 2 - this.bedHouseLength / 2);
        BlockPos houseCorner2BlockPos = houseCorner1BlockPos.add(this.bedHouseWidth, 0, this.bedHouseLength);
        ChunkPos houseCorner1ChunkPos = new ChunkPos(houseCorner1BlockPos);
        ChunkPos houseCorner2ChunkPos = new ChunkPos(houseCorner2BlockPos);
        // Test all the chunks that the house will be in, ensure that all chunks that the house will be in are of the correct type
        boolean houseValid = this.processChunks(new IChunkProcessor<Boolean>()
        {
            // Compute the minimum and maximum height over all the chunks that the dark forest house will cross over
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;

            @Override
            public boolean processChunk(ChunkPos chunkPos)
            {
                Set<Biome> biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z);
                // Dark forest can only spawn in in plains or savannahs
                if (COMPATIBLE_HOUSE_BIOMES.containsAll(biomes))
                {
                    // Compute min and max height
                    minHeight = Math.min(minHeight, heightmap.getLowestHeight(chunkPos));
                    maxHeight = Math.max(maxHeight, heightmap.getHighestHeight(chunkPos));
                    return true;
                }
                else
                {
                    return false;
                }
            }

            @Override
            public Boolean getResult()
            {
                // If the height difference is less than 8 then it's OK to place the house here
                return (maxHeight - minHeight) < 8;
            }

            @Override
            public Boolean getDefaultResult()
            {
                return false;
            }
        }, () ->
        {
            // Go over all chunks that the bed house would cover and check them
            List<ChunkPos> houseChunks = Lists.newArrayList();
            for (int chunkX = houseCorner1ChunkPos.x; chunkX <= houseCorner1ChunkPos.x; chunkX++)
                for (int chunkZ = houseCorner2ChunkPos.z; chunkZ <= houseCorner2ChunkPos.z; chunkZ++)
                    houseChunks.add(new ChunkPos(chunkX, chunkZ));
            return houseChunks;
        });

        // If the house isn't valid don't place a dark forest here
        if (!houseValid)
        {
            return 0;
        }
        else
        // If the house is valid we're good to go, the chance to gen will be .23%
        {
            return 0.0023 * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getDarkForestMultiplier();
        }
    }

    /**
     * Generates the structure at a position with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     Data containing info about structure placement like tree locations and prop positions
     */
    @Override
    public void generate(World world, ChunkPos chunkPos, NBTTagCompound data)
    {
        // Create props first since they're least important and can be overridden
        NBTTagList props = data.getTagList(NBT_PROPS, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < props.tagCount(); i++)
        {
            NBTTagCompound schematicNBT = props.getCompoundTagAt(i);
            // Grab the schematic ID to generate
            int schematicId = schematicNBT.getInteger(NBT_SCHEMATIC_ID);
            // Grab the schematic to generate
            Schematic schematic = ModSchematics.DARK_FOREST_PROPS[schematicId];
            // Grab the schematic position
            BlockPos schematicPos = NBTUtil.getPosFromTag(schematicNBT.getCompoundTag(NBT_POSITION));
            // Generate the schematic
            SchematicGenerator.generateSchematic(schematic, world, schematicPos, chunkPos);
        }

        // Create trees second since they shouldn't override the house but can override props
        NBTTagList trees = data.getTagList(NBT_TREES, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < trees.tagCount(); i++)
        {
            NBTTagCompound schematicNBT = trees.getCompoundTagAt(i);
            // Grab the schematic ID to generate
            int schematicId = schematicNBT.getInteger(NBT_SCHEMATIC_ID);
            // Grab the schematic to generate
            Schematic schematic = ModSchematics.DARK_FOREST_TREES[schematicId];
            // Grab the schematic position
            BlockPos schematicPos = NBTUtil.getPosFromTag(schematicNBT.getCompoundTag(NBT_POSITION));
            // Generate the schematic
            SchematicGenerator.generateSchematic(schematic, world, schematicPos, chunkPos);
        }

        // Generate the bed house in the center last
        BlockPos housePos = NBTUtil.getPosFromTag(data.getCompoundTag(NBT_HOUSE_POSITION));
        SchematicGenerator.generateSchematic(ModSchematics.BED_HOUSE, world, housePos, chunkPos, ModLootTables.DARK_FOREST);
    }


    /**
     * Store the prop and tree data into the NBT compound
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider The biome provider used to generate props on dry land only
     * @return The tree and prop positions used in generation and structure position
     */
    @Override
    public NBTTagCompound generateStructureData(World world, BlockPos blockPos, BiomeProvider biomeProvider)
    {
        /*
        Dark forest layout:
           ________________________________
          |                                |
          |            t gutter            | l
          |          ____________          | e
          |         |            |         | n
          | l gutter|   house    | r gutter| g
          |         |            |         | t
          |         |____________|         | h
          |                                |
          |            b gutter            |
          |________________________________|
                        width
         */

        // Compute the rectangles of each gutter -> x,y and width,height

        // One rectangle for each gutter
        Rectangle leftGutter = new Rectangle(0, 0, (this.getXWidth() - this.bedHouseWidth) / 2, this.getZLength());
        Rectangle rightGutter = new Rectangle(this.bedHouseWidth + leftGutter.width, 0, leftGutter.width, this.getZLength());
        Rectangle bottomGutter = new Rectangle(0, 0, this.getXWidth(), (this.getZLength() - this.bedHouseLength) / 2);
        Rectangle topGutter = new Rectangle(0, this.bedHouseLength + bottomGutter.height, this.getXWidth(), (this.getZLength() - this.bedHouseLength) / 2);
        // A list of gutters
        List<Rectangle> gutters = ImmutableList.of(leftGutter, rightGutter, bottomGutter, topGutter);
        List<EnumFacing> bedHouseSides = ImmutableList.of(EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH);

        // Add random props, trees, and the bed house into the data
        IHeightmap heightmap = OverworldHeightmap.get(world);
        NBTTagCompound data = new NBTTagCompound();
        addRandomProps(data, heightmap, blockPos, biomeProvider, gutters);
        addRandomTrees(data, heightmap, blockPos, gutters, bedHouseSides);
        setBedHousePosition(data, heightmap, blockPos);
        // Finally set the position of the structure to be at the same level as the house
        int houseY = NBTUtil.getPosFromTag(data.getCompoundTag(NBT_HOUSE_POSITION)).getY();
        data.setTag(NBT_POSITION, NBTUtil.createPosTag(new BlockPos(blockPos.getX(), houseY, blockPos.getZ())));

        return data;
    }

    /**
     * Sets the bed house NBT data
     *
     * @param data      The data to set forest be house position into
     * @param heightmap A heightmap to use to compute position
     * @param blockPos  The position the dark forest was placed at
     */
    private void setBedHousePosition(NBTTagCompound data, IHeightmap heightmap, BlockPos blockPos)
    {
        // Compute the house's y level so that it sits flat
        // Compute the two chunk position corners of the house
        BlockPos houseCorner1BlockPos = blockPos.add(this.getXWidth() / 2 - this.bedHouseWidth / 2, 0, this.getZLength() / 2 - this.bedHouseLength / 2);
        BlockPos houseCorner2BlockPos = houseCorner1BlockPos.add(this.bedHouseWidth, 0, this.bedHouseLength);

        // Find the lowest y value of the chunks covered by the bed house
        ChunkPos houseCorner1ChunkPos = new ChunkPos(houseCorner1BlockPos);
        ChunkPos houseCorner2ChunkPos = new ChunkPos(houseCorner2BlockPos);

        Integer minGroundHeight = this.processChunks(new LowestHeightChunkProcessor(heightmap), () ->
        {
            // Go over all chunks that the bed house is covering
            List<ChunkPos> houseChunks = Lists.newArrayList();
            for (int chunkX = houseCorner1ChunkPos.x; chunkX <= houseCorner2ChunkPos.x; chunkX++)
                for (int chunkZ = houseCorner1ChunkPos.z; chunkZ <= houseCorner2ChunkPos.z; chunkZ++)
                    houseChunks.add(new ChunkPos(chunkX, chunkZ));
            return houseChunks;
        });

        // Get the house position and set it in the data nbt
        BlockPos housePos = new BlockPos(houseCorner1BlockPos.getX(), minGroundHeight, houseCorner1BlockPos.getZ());

        // Store the house's position into the NBT
        data.setTag(NBT_HOUSE_POSITION, NBTUtil.createPosTag(housePos));
    }

    /**
     * Adds between 25 - 100 random props in the world. These are small objects like stumps, logs, saplings, etc
     *
     * @param data          The data to add the props to
     * @param heightmap     The heightmap of the world to create props in
     * @param blockPos      The position to generate the structure at
     * @param biomeProvider The biome provider used to get information about the biomes a prop will be placed in
     * @param gutters       The gutters around the bed house to generate in
     */
    private void addRandomProps(NBTTagCompound data, IHeightmap heightmap, BlockPos blockPos, BiomeProvider biomeProvider, List<Rectangle> gutters)
    {
        NBTTagList props = new NBTTagList();
        // The number of props to generate
        int NUMBER_OF_PROPS = RandomUtils.nextInt(25, 100);
        for (int i = 0; i < NUMBER_OF_PROPS; i++)
        {
            // Get a random prop schematic ID
            int schematicId = RandomUtils.nextInt(0, ModSchematics.DARK_FOREST_PROPS.length);
            // Get that schematic
            Schematic schematic = ModSchematics.DARK_FOREST_PROPS[schematicId];
            // Get a random gutter to place it in
            Rectangle gutter = gutters.get(RandomUtils.nextInt(0, gutters.size()));

            // Compute the x,z position the schematic should be at
            BlockPos schematicPosNoY = getRandomPropPosition(schematic, blockPos, gutter);
            ChunkPos chunkPos = new ChunkPos(schematicPosNoY);
            // Figure out if this position is valid (no water under props)
            Set<Biome> biomes = this.approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z);
            if (INCOMPATIBLE_BIOMES.stream().anyMatch(biomes::contains))
            {
                continue;
            }

            // Get the low height in the center chunk of the schematic and place the schematic there.
            int yPos = heightmap.getLowestHeight(new ChunkPos(schematicPosNoY.add(schematic.getWidth() / 2, 0, schematic.getLength() / 2)));
            // Update the Y value
            BlockPos schematicPos = new BlockPos(schematicPosNoY.getX(), yPos, schematicPosNoY.getZ());

            // Create the prop tag and append it
            NBTTagCompound prop = new NBTTagCompound();
            prop.setInteger(NBT_SCHEMATIC_ID, schematicId);
            prop.setTag(NBT_POSITION, NBTUtil.createPosTag(schematicPos));
            props.appendTag(prop);
        }
        data.setTag(NBT_PROPS, props);
    }

    /**
     * Gets a random prop position for a schematic and a gutter
     *
     * @param prop     The prop schematic to generate
     * @param blockPos The base position of the schematic
     * @param gutter   The gutter to put the prop in
     * @return The position that this prop will be placed at in the world
     */
    private BlockPos getRandomPropPosition(Schematic prop, BlockPos blockPos, Rectangle gutter)
    {
        // Ensure the schematic fits perfectly into the gutter without going outside
        int xMin = gutter.x;
        int xMax = gutter.x + gutter.width - prop.getWidth();
        int zMin = gutter.y;
        int zMax = gutter.y + gutter.height - prop.getLength();
        // Return a position in the valid range
        return new BlockPos(blockPos.getX() + RandomUtils.nextInt(xMin, xMax), 0, blockPos.getZ() + RandomUtils.nextInt(zMin, zMax));
    }

    /**
     * Adds between 10 - 20 random trees in the world around the bed house
     *
     * @param data          The data to add the props to
     * @param heightmap     The heightmap of the world to create trees in
     * @param blockPos      The position to generate the structure at
     * @param gutters       The gutters around the bed house to generate in
     * @param bedHouseSides A parallel list to gutters with the sides that the bed house is at for each gutter
     */
    private void addRandomTrees(NBTTagCompound data, IHeightmap heightmap, BlockPos blockPos, List<Rectangle> gutters, List<EnumFacing> bedHouseSides)
    {
        NBTTagList trees = new NBTTagList();
        // The number of trees to generate
        int NUMBER_OF_TREES = RandomUtils.nextInt(10, 20);
        for (int i = 0; i < NUMBER_OF_TREES; i++)
        {
            // Get a random tree schematic ID
            int schematicId = RandomUtils.nextInt(0, ModSchematics.DARK_FOREST_TREES.length);
            // Get that schematic
            Schematic schematic = ModSchematics.DARK_FOREST_TREES[schematicId];
            // Get a random gutter to place it in
            int gutterIndex = RandomUtils.nextInt(0, gutters.size());
            Rectangle gutter = gutters.get(gutterIndex);
            EnumFacing bedHouseSide = bedHouseSides.get(gutterIndex);
            // Compute the x,z position the schematic should be at
            BlockPos schematicPosNoY = getRandomTreePosition(schematic, blockPos, gutter, bedHouseSide);
            // Get the low height in the center chunk of the schematic and place the schematic there.
            int yPos = heightmap.getLowestHeight(new ChunkPos(schematicPosNoY.add(schematic.getWidth() / 2, 0, schematic.getLength() / 2)));
            // Trees need to have roots underground so move them down by 5
            yPos = yPos - 5;
            // Update the Y value
            BlockPos schematicPos = new BlockPos(schematicPosNoY.getX(), yPos, schematicPosNoY.getZ());

            // Create the tree tag and append it
            NBTTagCompound tree = new NBTTagCompound();
            tree.setInteger(NBT_SCHEMATIC_ID, schematicId);
            tree.setTag(NBT_POSITION, NBTUtil.createPosTag(schematicPos));
            trees.appendTag(tree);
        }

        // Set the trees and prop's lists
        data.setTag(NBT_TREES, trees);
    }

    /**
     * Gets a random tree position for a schematic and a gutter
     *
     * @param tree         The tree to generate
     * @param blockPos     The position the structure will be at
     * @param gutter       The gutter to place the tree trunk in
     * @param bedHouseSide The side that the bed house is on relative to the gutter. This is necessary since trees are allowed to
     *                     overlap with the house since the leaves may go over top
     * @return The position that this tree will be placed at
     */
    private BlockPos getRandomTreePosition(Schematic tree, BlockPos blockPos, Rectangle gutter, EnumFacing bedHouseSide)
    {
        int xMin;
        int xMax;
        int zMin;
        int zMax;

        // The amount of space to leave for the trunk
        final int TRUNK_SIZE = 5;

        // Compute the min and max positions assuming
        // 1) The tree is not centered
        // 2) The bed house is not on any side
        xMax = gutter.x + gutter.width - tree.getWidth() / 2;
        xMin = gutter.x + tree.getWidth() / 2;
        zMax = gutter.y + gutter.height - tree.getLength() / 2;
        zMin = gutter.y + tree.getLength() / 2;

        // Adjust the proper value depending on which side the bed house is on.
        // Leave a buffer of TRUNK_SIZE so that trees don't go through the house
        switch (bedHouseSide)
        {
            case EAST:
                xMax = gutter.x + gutter.width - TRUNK_SIZE;
                break;
            case WEST:
                xMin = gutter.x + TRUNK_SIZE;
                break;
            case NORTH:
                zMax = gutter.y + gutter.height - TRUNK_SIZE;
                break;
            case SOUTH:
                zMin = gutter.y + TRUNK_SIZE;
                break;
        }
        // Move the schematic so that the tree is centered in the position
        xMin = xMin - tree.getWidth() / 2;
        xMax = xMax - tree.getWidth() / 2;
        zMin = zMin - tree.getLength() / 2;
        zMax = zMax - tree.getLength() / 2;

        // Return a random valid position
        return new BlockPos(blockPos.getX() + RandomUtils.nextInt(xMin, xMax), 0, blockPos.getZ() + RandomUtils.nextInt(zMin, zMax));
    }

    /**
     * @return The width of the structure in blocks
     */
    @Override
    public int getXWidth()
    {
        return this.width;
    }

    /**
     * @return The length of the structure in blocks
     */
    @Override
    public int getZLength()
    {
        return this.height;
    }
}
