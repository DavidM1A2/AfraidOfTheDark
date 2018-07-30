package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.reference.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register item event and registers all of our items
 */
public class ItemRegister
{
	/**
	 * Called by forge to register any of our items
	 *
	 * @param event The event to register to
	 */
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ItemBlock(ModBlocks.GRAVEWOOD_SAPLING).setRegistryName(ModBlocks.GRAVEWOOD_SAPLING.getRegistryName()));
	}
}
