package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.block.gravewood.BlockGravewoodSapling;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

		registry.register(new BlockGravewoodSapling());
	}
}
