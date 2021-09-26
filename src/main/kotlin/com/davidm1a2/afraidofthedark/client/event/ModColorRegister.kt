package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeavesBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.FoliageColors
import net.minecraft.world.IBlockDisplayReader
import net.minecraft.world.biome.BiomeColors
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

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
    fun registerBlockColors(event: ColorHandlerEvent.Block) {
        // Filter our block list by leaf blocks only
        val leafBlocks = ModBlocks.BLOCK_LIST.filterIsInstance<AOTDLeavesBlock>().toTypedArray()

        // The block colors to register to
        val blockColors = event.blockColors

        // Register a block color handler so that leaf blocks are colored properly when placed
        blockColors.register(
            IBlockColor { _: BlockState, blockAccess: IBlockDisplayReader?, pos: BlockPos?, _: Int ->
                // Make sure we were passed valid parameters
                return@IBlockColor if (blockAccess != null && pos != null) {
                    BiomeColors.getAverageFoliageColor(blockAccess, pos)
                } else {
                    FoliageColors.getDefaultColor()
                }
            },
            *leafBlocks
        )
    }

    /**
     * Registers block item color handlers for leaves and grass blocks
     *
     * @param event The event to register item colors to
     */
    @SubscribeEvent
    fun registerItemColors(event: ColorHandlerEvent.Item) {
        // Filter our block list by leaf blocks only
        val leafBlocks = ModBlocks.BLOCK_LIST.filterIsInstance<AOTDLeavesBlock>().toTypedArray()

        // The item and block colors to register to
        val itemColors = event.itemColors
        val blockColors = event.blockColors

        // Register an item color handler so that leaf blocks are colored properly when held in the inventory
        itemColors.register(
            { stack: ItemStack, tintIndex: Int ->
                // Grab the state of the block if it was placed in the world
                val iBlockState = (stack.item as BlockItem).block.defaultBlockState()
                blockColors.getColor(iBlockState, null, null, tintIndex)
            }, *leafBlocks
        )
    }
}
