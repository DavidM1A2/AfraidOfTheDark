package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockLeaves
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ColorizerFoliage
import net.minecraft.world.IBlockAccess
import net.minecraft.world.biome.BiomeColorHelper
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Color register registers item and block colors for leaves and grass blocks
 */
class ModColorRegister {
    /**
     * Registers block color handlers for leaves and grass blocks
     *
     * @param event The event to register block colors to
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun registerBlockColors(event: ColorHandlerEvent.Block) {
        // Filter our block list by leaf blocks only
        val leafBlocks = ModBlocks.BLOCK_LIST.filterIsInstance<BlockLeaves>().toTypedArray()

        // The block colors to register to
        val blockColors = event.blockColors

        // Register a block color handler so that leaf blocks are colored properly when placed
        blockColors.registerBlockColorHandler(
            IBlockColor { _: IBlockState, blockAccess: IBlockAccess?, pos: BlockPos?, _: Int ->
                // Make sure we were passed valid parameters
                if (blockAccess != null && pos != null) {
                    // Return the color at the position
                    return@IBlockColor BiomeColorHelper.getFoliageColorAtPos(blockAccess, pos)
                }
                ColorizerFoliage.getFoliageColor(0.5, 1.0)
            }, *leafBlocks
        )
    }

    /**
     * Registers block item color handlers for leaves and grass blocks
     *
     * @param event The event to register item colors to
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun registerItemColors(event: ColorHandlerEvent.Item) {
        // Filter our block list by leaf blocks only
        val leafBlocks = ModBlocks.BLOCK_LIST.filterIsInstance<BlockLeaves>().toTypedArray()

        // The item and block colors to register to
        val itemColors = event.itemColors
        val blockColors = event.blockColors

        // Register an item color handler so that leaf blocks are colored properly when held in the inventory
        itemColors.registerItemColorHandler(
            IItemColor { stack: ItemStack, tintIndex: Int ->
                // Grab the state of the block if it was placed in the world
                @Suppress("DEPRECATION")
                val iBlockState = (stack.item as ItemBlock).block.getStateFromMeta(stack.metadata)
                blockColors.colorMultiplier(iBlockState, null, null, tintIndex)
            }, *leafBlocks
        )
    }
}