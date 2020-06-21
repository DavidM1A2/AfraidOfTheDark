package com.davidm1a2.afraidofthedark.common.dimension

import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDIslandData
import com.davidm1a2.afraidofthedark.common.capabilities.world.IslandVisitorData
import net.minecraft.block.BlockAir
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Utility methods that deal with island index related things
 */
object IslandUtility {
    /**
     * Called to get a player's island positional index. If none is present one will be computed first.
     *
     * @param world      The world that the player needs a positional index for
     * @param islandData The island data to compute position on
     * @return The position in the void chest that this player owns
     */
    fun getOrAssignPlayerPositionalIndex(world: World, islandData: IAOTDIslandData): Int {
        // -1 means unassigned, we need to compute the index first
        if (islandData.positionalIndex == -1) {
            // Compute this new player's index
            val playersNewPositionalIndex = IslandVisitorData.get(world)!!.addAndReturnNewVisitor()
            // Set that new index
            islandData.positionalIndex = playersNewPositionalIndex
        }
        return islandData.positionalIndex
    }

    /**
     * Tests a given position in a world to see if it's a valid spawn location for the player
     *
     * @param world          The world to test
     * @param blockPos       The position that will be the center to test around
     * @param searchDistance The maximum distance to search for a spawn position
     * @return The block position that the player should spawn at, or null if it doesn't exist
     */
    fun findValidSpawnLocation(world: World, blockPos: BlockPos, searchDistance: Int): BlockPos? {
        val xCenter = blockPos.x
        val yCenter = blockPos.y
        val zCenter = blockPos.z
        // Iterate over all x,y,z positions +/- DISTANCE / 2 distance away from the center. If it's valid return it
        for (x in xCenter - searchDistance / 2 until xCenter + searchDistance / 2) {
            for (y in yCenter - searchDistance / 2 until yCenter + searchDistance / 2) {
                for (z in zCenter - searchDistance / 2 until zCenter + searchDistance / 2) {
                    if (isValidSpawnLocation(world, BlockPos(x, y, z))) {
                        return BlockPos(x, y, z)
                    }
                }
            }
        }
        return null
    }

    /**
     * Tests if a given x,y,z spot in the world is a valid place to spawn at
     *
     * @param world    The world to test
     * @param blockPos The position to test
     * @return True if the spot has a solid floor and air above, false otherwise
     */
    fun isValidSpawnLocation(world: World, blockPos: BlockPos): Boolean {
        val bottomBlock = world.getBlockState(blockPos)
        val bottomBlockMaterial = bottomBlock.material
        // If the material is solid and blocks movement it's valid
        return if (bottomBlockMaterial.isSolid && bottomBlockMaterial.blocksMovement()) {
            // Ensure the two blocks above are air
            world.getBlockState(blockPos.up()).block is BlockAir && world.getBlockState(blockPos.up(2)).block is BlockAir
        } else false
    }
}