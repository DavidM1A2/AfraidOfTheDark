package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that registers all furnace fuels
 */
public class FurnaceFuelRegister
{
	// Mapping of block -> burn time
	private static final Map<Block, Integer> ITEM_TO_BURN_TIME = new HashMap<Block, Integer>()
	{{
		put(ModBlocks.GRAVEWOOD_SAPLING, 100);
	}};

	/**
	 * Called to make certain blocks furnace smeltable
	 *
	 * @param event The event containing the block/item smelting info
	 */
	@SubscribeEvent
	public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event)
	{
		// If we know of the burn time for the item then set the burn time for the item based on the hashmap
		if (ITEM_TO_BURN_TIME.containsKey(Block.getBlockFromItem(event.getItemStack().getItem())))
			event.setBurnTime(ITEM_TO_BURN_TIME.get(Block.getBlockFromItem(event.getItemStack().getItem())));
	}
}
