package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.item.Item
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that registers all furnace fuels
 */
class FurnaceFuelRegister {
    // Mapping of block -> burn time
    private lateinit var itemToBurnTime: Map<Item, Int>

    /**
     * Called to make certain blocks furnace smeltable
     *
     * @param event The event containing the block/item smelting info
     */
    @SubscribeEvent
    fun onFurnaceFuelBurnTimeEvent(event: FurnaceFuelBurnTimeEvent) {
        if (!::itemToBurnTime.isInitialized) {
            itemToBurnTime = mapOf(
                ModBlocks.GRAVEWOOD_SAPLING.asItem() to 100,
                ModBlocks.GRAVEWOOD_FENCE.asItem() to 300,
                ModBlocks.GRAVEWOOD_FENCE_GATE.asItem() to 300,
                ModBlocks.MANGROVE_SAPLING.asItem() to 100,
                ModBlocks.MANGROVE_FENCE.asItem() to 300,
                ModBlocks.MANGROVE_FENCE_GATE.asItem() to 300,
                ModBlocks.SACRED_MANGROVE_SAPLING.asItem() to 1000,
                ModBlocks.SACRED_MANGROVE_FENCE.asItem() to 3000,
                ModBlocks.SACRED_MANGROVE_FENCE_GATE.asItem() to 3000
            )
        }

        // If we know of the burn time for the item then set the burn time for the item based on the hashmap
        event.burnTime = itemToBurnTime[event.itemStack.item] ?: return
    }
}