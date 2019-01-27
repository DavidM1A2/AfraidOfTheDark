/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

import com.DavidM1A2.afraidofthedark.client.entity.bolt.RenderWoodenBolt;
import com.DavidM1A2.afraidofthedark.client.entity.enchantedSkeleton.RenderEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.client.keybindings.ModKeybindings;
import com.DavidM1A2.afraidofthedark.common.block.core.AOTDLeaves;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModEntities;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityWoodenBolt;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.handler.ResearchOverlayHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;

/**
 * Proxy that is only to be instantiated on the CLIENT side
 */
public class ClientProxy extends CommonProxy
{
	// Research overlay handler used to show when a player unlocks a research
	private final ResearchOverlayHandler RESEARCH_OVERLAY_HANDLER = new ResearchOverlayHandler();

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
		for (Pair<EntityEntry, IRenderFactory> renderingEntry : ModEntities.ENTITY_RENDERERS)
			RenderingRegistry.registerEntityRenderingHandler(renderingEntry.getKey().getEntityClass(), renderingEntry.getValue());
	}

	/**
	 * Called to register any key bindings
	 */
	@Override
	public void registerKeyBindings()
	{
		ClientRegistry.registerKeyBinding(ModKeybindings.FIRE_WRIST_CROSSBOW);
		ClientRegistry.registerKeyBinding(ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY);
	}

	/**
	 * @return The research overlay handler client side
	 */
	@Override
	public ResearchOverlayHandler getResearchOverlay()
	{
		return RESEARCH_OVERLAY_HANDLER;
	}
}
