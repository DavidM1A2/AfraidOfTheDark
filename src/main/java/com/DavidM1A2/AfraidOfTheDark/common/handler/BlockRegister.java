package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register block event and registers all of our blocks
 */
public class BlockRegister
{
	/**
	 * Called by forge to register any of our blocks
	 *
	 * @param event The event to register to
	 */
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		// Register all blocks in our mod
		registry.registerAll(ModBlocks.BLOCK_LIST);
	}

	/**
	 * Called by forge to register any of our block renderers
	 *
	 * @param event The event that signifies that ModelLoader is ready to receive blocks
	 */
	@SubscribeEvent
	public void registerBlockRenderers(ModelRegistryEvent event)
	{
		// Register models for all blocks in our mod
		for (Block block : ModBlocks.BLOCK_LIST)
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
