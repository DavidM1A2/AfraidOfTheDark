package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.Block
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that registers all furnace fuels
 */
class FurnaceFuelRegister {
    /**
     * Called to make certain blocks furnace smeltable
     *
     * @param event The event containing the block/item smelting info
     */
    @SubscribeEvent
    fun onFurnaceFuelBurnTimeEvent(event: FurnaceFuelBurnTimeEvent) {
        // If we know of the burn time for the item then set the burn time for the item based on the hashmap
        event.burnTime = ITEM_TO_BURN_TIME[Block.getBlockFromItem(event.itemStack.item)] ?: return
    }

    companion object {
        // Mapping of block -> burn time
        private val ITEM_TO_BURN_TIME = mapOf(
            ModBlocks.GRAVEWOOD_SAPLING to 100,
            ModBlocks.MANGROVE_SAPLING to 100
        )
    }
}