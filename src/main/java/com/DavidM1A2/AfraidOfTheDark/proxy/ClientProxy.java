/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

import com.DavidM1A2.afraidofthedark.client.entity.enchantedSkeleton.RenderEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.block.core.AOTDLeaves;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Arrays;

/**
 * Proxy that is only to be instantiated on the CLIENT side
 */
public class ClientProxy extends CommonProxy
{
	/**
	 * Called to initialize leaf block renderers. This simply ensures the colors of the leaves are correctly applied
	 */
	@Override
	public void initializeLeafRenderers()
	{
		// Grab a reference to the block and item colors objects
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

		// Filter our block list by leaf blocks only
		Block[] leafBlocks = Arrays.stream(ModBlocks.BLOCK_LIST).filter(block -> block instanceof AOTDLeaves).toArray(Block[]::new);

		// Register a block color handler so that leaf blocks are colored properly when placed
		blockColors.registerBlockColorHandler((state, blockAccess, pos, tintIndex) ->
		{
			// Make sure we were passed valid parameters
			if (blockAccess != null && pos != null)
				// Return the color at the position
				return BiomeColorHelper.getFoliageColorAtPos(blockAccess, pos);
			// Return a default if the parameters were bad
			return ColorizerFoliage.getFoliageColor(0.5, 1.0);
		}, leafBlocks);

		// Register an item color handler so that leaf blocks are colored properly when held in the inventory
		itemColors.registerItemColorHandler((stack, tintIndex) ->
		{
			// Grab the state of the block if it was placed in the world
			IBlockState iBlockState = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			// Use our block color and apply it to the item
			return blockColors.colorMultiplier(iBlockState, null, null, tintIndex);
		}, leafBlocks);
	}

	/**
	 * Called to initialize entity renderers
	 */
	@Override
	public void initializeEntityRenderers()
	{
		// Register all of our renderers
		RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class, RenderEnchantedSkeleton::new);
	}
}
