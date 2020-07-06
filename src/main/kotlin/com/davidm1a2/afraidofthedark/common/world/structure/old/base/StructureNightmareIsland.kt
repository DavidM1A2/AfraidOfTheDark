package com.davidm1a2.afraidofthedark.common.world.structure.old.base

import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.generateSchematic
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.provider.BiomeProvider

/**
 * Class representing the nightmare island structure, it will not generate naturally
 *
 * @constructor sets the structure name
 */
class StructureNightmareIsland : AOTDStructure("nightmare_island") {
    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return 0, this structure does not generate
     */
    override fun computeChanceToGenerateAt(
        blockPos: BlockPos,
        heightmap: IHeightmap,
        biomeProvider: BiomeProvider
    ): Double {
        return 0.0
    }

    /**
     * Generates the structure with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     Any additional structure data that is needed for generation such as structure position
     */
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound) {
        // Get the position of the structure from the data compound
        val blockPos = getPosition(data)

        // This structure is simple, it is just the crypt schematic
        world.generateSchematic(ModSchematics.NIGHTMARE_ISLAND, blockPos, chunkPos, ModLootTables.NIGHTMARE_ISLAND)
    }

    /**
     * Called to generate a random permutation of the structure. Set the structure's position
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
     */
    override fun generateStructureData(world: World, blockPos: BlockPos, biomeProvider: BiomeProvider): NBTTagCompound {
        val compound = NBTTagCompound()
        // Set the position to the blockpos
        compound.setTag(NBT_POSITION, NBTUtil.writeBlockPos(blockPos))
        return compound
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int {
        return ModSchematics.NIGHTMARE_ISLAND.getWidth().toInt()
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int {
        return ModSchematics.NIGHTMARE_ISLAND.getLength().toInt()
    }
}