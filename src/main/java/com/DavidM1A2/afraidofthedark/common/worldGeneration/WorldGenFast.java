package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.BlockSnapshot;

/**
 * Utility class to perform special world operations more quickly than default MC like setblock
 */
public class WorldGenFast
{
    /**
     * Faster version of world.setBlockState() that does not perform any lighting computation or updates
     *
     * @param world    The world to set blocks in
     * @param pos      The position to set the block in
     * @param newState The new state of the block
     * @param flags    Any additional flags to pass down, see original setBlockState() for flag documentation
     * @return True if the block was set, false otherwise
     */
    public static boolean setBlockStateFast(World world, BlockPos pos, IBlockState newState, int flags)
    {
        /*
        Code copied and modified:
         */

        if (world.isOutsideBuildHeight(pos))
        {
            return false;
        }
        else if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            return false;
        }
        else
        {
            Chunk chunk = world.getChunkFromBlockCoords(pos);

            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            BlockSnapshot blockSnapshot = null;
            if (world.captureBlockSnapshots && !world.isRemote)
            {
                blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, pos, flags);
                world.capturedBlockSnapshots.add(blockSnapshot);
            }

            /*
            // Removed since we aren't updating light
            IBlockState oldState = world.getBlockState(pos);
            int oldLight = oldState.getLightValue(world, pos);
            int oldOpacity = oldState.getLightOpacity(world, pos);
             */

            // We need to modify how the chunk sets block state too, call our internal chunk set block state
            IBlockState iblockstate = setChunkBlockStateFast(chunk, pos, newState);

            if (iblockstate == null)
            {
                if (blockSnapshot != null)
                {
                    world.capturedBlockSnapshots.remove(blockSnapshot);
                }
                return false;
            }
            else
            {
                /*
                // Removed since we aren't updating light
                if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight)
                {
                    world.profiler.startSection("checkLight");
                    world.checkLight(pos);
                    world.profiler.endSection();
                }
                 */

                if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    world.markAndNotifyBlock(pos, chunk, iblockstate, newState, flags);
                }
                return true;
            }
        }
    }

    /**
     * Faster version of chunk.setBlockState() that does not perform any lighting computation or updates. It's copied
     * exactly with some code commented out
     *
     * @param chunk The chunk to perform updates on
     * @param pos   The blockpos to update
     * @param state The state to set
     * @return The set block state or null if the state wasn't set
     */
    private static IBlockState setChunkBlockStateFast(Chunk chunk, BlockPos pos, IBlockState state)
    {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;

        /*
        No need for this check, it's not lighting related but requires more effort and requires reflection so ignore it
        int l = k << 4 | i;

        if (j >= chunk.precipitationHeightMap[l] - 1)
        {
            chunk.precipitationHeightMap[l] = -999;
        }
         */

        /*
        // No need for any height information since we're not updating lighting
        int i1 = this.heightMap[l];
         */
        IBlockState iblockstate = chunk.getBlockState(pos);

        if (iblockstate == state)
        {
            return null;
        }
        else
        {
            Block block = state.getBlock();
            Block block1 = iblockstate.getBlock();
            /*
            Computes the new light value, we don't need this
            int k1 = iblockstate.getLightOpacity(chunk.getWorld(), pos); // Relocate old light value lookup here, so that it is called before TE is removed.
             */
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[j >> 4];
            /*
            This flag is used for height tests, ignore it too
            boolean flag = false;
             */

            if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE)
            {
                if (block == Blocks.AIR)
                {
                    return null;
                }

                extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, chunk.getWorld().provider.hasSkyLight());
                chunk.getBlockStorageArray()[j >> 4] = extendedblockstorage;
                /*
                This flag is used for height tests as mentioned above
                flag = j >= i1;
                 */
            }

            extendedblockstorage.set(i, j & 15, k, state);

            //if (block1 != block)
            {
                if (!chunk.getWorld().isRemote)
                {
                    if (block1 != block) //Only fire block breaks when the block changes.
                    {
                        block1.breakBlock(chunk.getWorld(), pos, iblockstate);
                    }
                    TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(chunk.getWorld(), pos, iblockstate, state))
                    {
                        chunk.getWorld().removeTileEntity(pos);
                    }
                }
                else if (block1.hasTileEntity(iblockstate))
                {
                    TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(chunk.getWorld(), pos, iblockstate, state))
                    {
                        chunk.getWorld().removeTileEntity(pos);
                    }
                }
            }

            if (extendedblockstorage.get(i, j & 15, k).getBlock() != block)
            {
                return null;
            }
            else
            {
                /*
                This does the actual light update, kill it with fire
                if (flag)
                {
                    this.generateSkylightMap();
                }
                else
                {
                    int j1 = state.getLightOpacity(chunk.getWorld(), pos);

                    if (j1 > 0)
                    {
                        if (j >= i1)
                        {
                            chunk.relightBlock(i, j + 1, k);
                        }
                    }
                    else if (j == i1 - 1)
                    {
                        chunk.relightBlock(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || chunk.getLightFor(EnumSkyBlock.SKY, pos) > 0 || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                        chunk.propagateSkylightOcclusion(i, k);
                    }
                }
                 */

                // If capturing blocks, only run block physics for TE's. Non-TE's are handled in ForgeHooks.onPlaceItemIntoWorld
                if (!chunk.getWorld().isRemote && block1 != block && (!chunk.getWorld().captureBlockSnapshots || block.hasTileEntity(state)))
                {
                    block.onBlockAdded(chunk.getWorld(), pos, state);
                }

                if (block.hasTileEntity(state))
                {
                    TileEntity tileentity1 = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity1 == null)
                    {
                        tileentity1 = block.createTileEntity(chunk.getWorld(), state);
                        chunk.getWorld().setTileEntity(pos, tileentity1);
                    }

                    if (tileentity1 != null)
                    {
                        tileentity1.updateContainingBlockInfo();
                    }
                }

                chunk.markDirty();
                return iblockstate;
            }
        }
    }
}
