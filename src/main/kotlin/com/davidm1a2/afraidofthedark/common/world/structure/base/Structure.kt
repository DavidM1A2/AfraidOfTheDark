package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraftforge.registries.ForgeRegistryEntry

/**
 * Base class for all structures in the game that are generated using schematics
 */
abstract class Structure : ForgeRegistryEntry<Structure>() {
    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return A value between 0 and 1 which is the chance between 0% and 100% that a structure could spawn at the given position
     */
    abstract fun computeChanceToGenerateAt(
        blockPos: BlockPos,
        heightmap: IHeightmap,
        biomeProvider: BiomeProvider
    ): Double

    /**
     * Generates the structure with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     Any additional structure data that is needed for generation such as structure position
     */
    abstract fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound)

    /**
     * Called to generate a random permutation of the structure. This is useful when the structure requires
     * random parameters to be set before starting generation.
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider A biome provider to be used if biome information is needed to generate structure data
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate. Default returns nbt with position
     */
    open fun generateStructureData(world: World, blockPos: BlockPos, biomeProvider: BiomeProvider): NBTTagCompound {
        val compound = NBTTagCompound()
        // Set the position to the blockpos
        compound.setTag(NBT_POSITION, NBTUtil.writeBlockPos(blockPos))
        return compound
    }

    /**
     * Extracts the block pos of the structure from the nbt data
     *
     * @param data The raw NBT data
     * @return The blockpos contained in the data
     */
    fun getPosition(data: NBTTagCompound): BlockPos {
        return NBTUtil.readBlockPos(data.getCompound(NBT_POSITION))
    }

    /**
     * @return The width of the structure in blocks
     */
    abstract fun getXWidth(): Int

    /**
     * @return The length of the structure in blocks
     */
    abstract fun getZLength(): Int

    /**
     * @return The unlocalized name of the structure
     */
    fun getUnlocalizedName(): String {
        return "structure.${registryName!!.namespace}.${registryName!!.path}"
    }

    companion object {
        // A constant NBT tag for structure position
        const val NBT_POSITION = "position"
    }
}