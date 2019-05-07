package com.DavidM1A2.afraidofthedark.common.dimension;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDIslandData;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.IslandVisitorData;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Utility methods that deal with island index related things
 */
public class IslandUtility
{
    /**
     * Called to get a player's island positional index. If none is present one will be computed first.
     *
     * @param world      The world that the player needs a positional index for
     * @param islandData The island data to compute position on
     * @return The position in the void chest that this player owns
     */
    public static int getOrAssignPlayerPositionalIndex(World world, IAOTDIslandData islandData)
    {
        // -1 means unassigned, we need to compute the index first
        if (islandData.getPositionalIndex() == -1)
        {
            // Compute this new player's index
            int playersNewPositionalIndex = IslandVisitorData.get(world).addAndReturnNewVisitor();
            // Set that new index
            islandData.setPositionalIndex(playersNewPositionalIndex);
        }
        return islandData.getPositionalIndex();
    }

    /**
     * Tests a given position in a world to see if it's a valid spawn location for the player
     *
     * @param world          The world to test
     * @param blockPos       The position that will be the center to test around
     * @param searchDistance The maximum distance to search for a spawn position
     * @return The block position that the player should spawn at, or null if it doesn't exist
     */
    public static BlockPos findValidSpawnLocation(World world, BlockPos blockPos, int searchDistance)
    {
        int xCenter = blockPos.getX();
        int yCenter = blockPos.getY();
        int zCenter = blockPos.getZ();
        // Iterate over all x,y,z positions +/- DISTANCE / 2 distance away from the center. If it's valid return it
        for (int x = xCenter - searchDistance / 2; x < xCenter + searchDistance / 2; x++)
            for (int y = yCenter - searchDistance / 2; y < yCenter + searchDistance / 2; y++)
                for (int z = zCenter - searchDistance / 2; z < zCenter + searchDistance / 2; z++)
                    if (IslandUtility.isValidSpawnLocation(world, new BlockPos(x, y, z)))
                        return new BlockPos(x, y, z);
        return null;
    }

    /**
     * Tests if a given x,y,z spot in the world is a valid place to spawn at
     *
     * @param world    The world to test
     * @param blockPos The position to test
     * @return True if the spot has a solid floor and air above, false otherwise
     */
    public static boolean isValidSpawnLocation(World world, BlockPos blockPos)
    {
        IBlockState bottomBlock = world.getBlockState(blockPos);
        Material bottomBlockMaterial = bottomBlock.getMaterial();
        // If the material is solid and blocks movement it's valid
        if (bottomBlockMaterial.isSolid() && bottomBlockMaterial.blocksMovement())
            // Ensure the two blocks above are air
            return world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir && world.getBlockState(blockPos.up(2)).getBlock() instanceof BlockAir;
        return false;
    }
}
