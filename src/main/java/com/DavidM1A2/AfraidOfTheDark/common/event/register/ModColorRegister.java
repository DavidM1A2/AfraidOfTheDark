package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDLeaves;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

/**
 * Color register registers item and block colors for leaves and grass blocks
 */
public class ModColorRegister
{
    /**
     * Registers block color handlers for leaves and grass blocks
     *
     * @param event The event to register block colors to
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerBlockColors(ColorHandlerEvent.Block event)
    {
        // Filter our block list by leaf blocks only
        Block[] leafBlocks = Arrays.stream(ModBlocks.BLOCK_LIST).filter(block -> block instanceof AOTDLeaves).toArray(Block[]::new);

        // The block colors to register to
        BlockColors blockColors = event.getBlockColors();

        // Register a block color handler so that leaf blocks are colored properly when placed
        blockColors.registerBlockColorHandler((state, blockAccess, pos, tintIndex) ->
        {
            // Make sure we were passed valid parameters
            if (blockAccess != null && pos != null)
            // Return the color at the position
            {
                return BiomeColorHelper.getFoliageColorAtPos(blockAccess, pos);
            }
            // Return a default if the parameters were bad
            return ColorizerFoliage.getFoliageColor(0.5, 1.0);
        }, leafBlocks);
    }

    /**
     * Registers block item color handlers for leaves and grass blocks
     *
     * @param event The event to register item colors to
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerItemColors(ColorHandlerEvent.Item event)
    {
        // Filter our block list by leaf blocks only
        Block[] leafBlocks = Arrays.stream(ModBlocks.BLOCK_LIST).filter(block -> block instanceof AOTDLeaves).toArray(Block[]::new);

        // The item and block colors to register to
        ItemColors itemColors = event.getItemColors();
        BlockColors blockColors = event.getBlockColors();

        // Register an item color handler so that leaf blocks are colored properly when held in the inventory
        itemColors.registerItemColorHandler((stack, tintIndex) ->
        {
            // Grab the state of the block if it was placed in the world
            IBlockState iBlockState = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            // Use our block color and apply it to the item
            return blockColors.colorMultiplier(iBlockState, null, null, tintIndex);
        }, leafBlocks);
    }
}
