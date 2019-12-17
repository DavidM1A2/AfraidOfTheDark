package com.davidm1a2.afraidofthedark.common.block.core

import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Base class for all AOTD blocks that also contain tile entities
 *
 * @constructor initializes the block's material, name, and tile entity flag
 * @param name     The name of the block
 * @param material The material of the block
 * @param displayInCreative True if the block should show up in creative, false otherwise
 */
abstract class AOTDBlockTileEntity(name: String, material: Material, displayInCreative: Boolean = false) : AOTDBlock(name, material, displayInCreative), ITileEntityProvider
{
    init
    {
        hasTileEntity = true
    }

    /**
     * Returns the render type for this block, tile entites do not render themselves, they use a special renderer instead
     *
     * @param state The block state to render
     * @return Invisible since the tile entity does not render itself
     */
    override fun getRenderType(state: IBlockState): EnumBlockRenderType
    {
        return EnumBlockRenderType.INVISIBLE
    }

    /**
     * Called when the block is broken, ensures the tile entity is removed as well
     *
     * @param worldIn The world the block was broken in
     * @param pos     The position the that the block is at
     * @param state   The state of the block
     */
    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState)
    {
        super.breakBlock(worldIn, pos, state)
        // Remove the tile entity!
        worldIn.removeTileEntity(pos)
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     *
     * @param state   The block state that received the event
     * @param worldIn The world the block is in
     * @param pos     The position the block is in
     * @param id      The ID of the tile entity
     * @param param   The event parameter, not sure what this does
     * @return True to call this on the client, false otherwise
     */
    @Suppress("DEPRECATION")
    override fun eventReceived(state: IBlockState, worldIn: World, pos: BlockPos, id: Int, param: Int): Boolean
    {
        super.eventReceived(state, worldIn, pos, id, param)
        val tileEntity = worldIn.getTileEntity(pos)
        return tileEntity != null && tileEntity.receiveClientEvent(id, param)
    }
}