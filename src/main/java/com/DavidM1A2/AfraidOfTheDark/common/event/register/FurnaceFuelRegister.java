package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        {
            event.setBurnTime(ITEM_TO_BURN_TIME.get(Block.getBlockFromItem(event.getItemStack().getItem())));
        }
    }
}
