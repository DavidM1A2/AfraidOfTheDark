package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getVoidChestData
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

/**
 * Class that defines a void chest portal block
 *
 * @constructor constructor sets item properties
 */
class BlockVoidChestPortal : AOTDBlock("void_chest_portal", Material.PORTAL, false)
{
    init
    {
        // This block gives off light
        setLightLevel(1.0f)
        // This block can't be broken
        setResistance(600000.0f)
        setBlockUnbreakable()
    }

    /**
     * Called when the entity walks into the block
     *
     * @param world       The world the block is in
     * @param blockPos    The position of the block
     * @param iBlockState The block state of the block
     * @param entity      The entity that walked into the block
     */
    override fun onEntityCollidedWithBlock(world: World, blockPos: BlockPos, iBlockState: IBlockState, entity: Entity)
    {
        // Server side processing only
        if (!world.isRemote)
        {
            // Test if the entity is a player
            if (entity is EntityPlayer)
            {
                // Grab the player's research and void chest data
                val playerResearch = entity.getResearch()
                val playerVoidChestData = entity.getVoidChestData()

                // If the player is in the void chest send them to their stored dimension
                if (world.provider.dimension == ModDimensions.VOID_CHEST.id)
                {
                    // Send the player to their previously stored dimension
                    entity.changeDimension(playerVoidChestData.preTeleportDimensionID, ModDimensions.NOOP_TELEPORTER)
                }
                else
                {
                    // If we can research the research research it
                    if (playerResearch.canResearch(ModResearches.VOID_CHEST))
                    {
                        playerResearch.setResearch(ModResearches.VOID_CHEST, true)
                        playerResearch.setResearch(ModResearches.ELDRITCH_DECORATION, true)
                        playerResearch.sync(entity, true)
                    }

                    // If the player has the void chest research then move the player
                    if (playerResearch.isResearched(ModResearches.VOID_CHEST))
                    {
                        // Make sure no friends index is set since the portal can only send to the player's dimension
                        playerVoidChestData.friendsIndex = -1
                        entity.changeDimension(ModDimensions.VOID_CHEST.id, ModDimensions.NOOP_TELEPORTER)
                    }
                }
            }
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     *
     * @param random The RNG to use
     * @return The number of blocks dropped, will always be 0 since you can't harvest this block
     */
    override fun quantityDropped(random: Random): Int
    {
        return 0
    }

    /**
     * Called to get the collision/hitbox of this block. We can walk through this block so return the null AABB
     *
     * @param blockState The block state of this block
     * @param worldIn    The world the block is in
     * @param pos        The position the block is at
     * @return The hitbox/bounding box of this block
     */
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB?
    {
        return NULL_AABB
    }

    /**
     * This block is not opaque since we can see through it
     *
     * @param state The block state
     * @return False since the block is not opaque
     */
    override fun isOpaqueCube(state: IBlockState): Boolean
    {
        return false
    }

    /**
     * @return The layer that this block is in, it's translucent since we can see through it
     */
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer
    {
        return BlockRenderLayer.TRANSLUCENT
    }
}