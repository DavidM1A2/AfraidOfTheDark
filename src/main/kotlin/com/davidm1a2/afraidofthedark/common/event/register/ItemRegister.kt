package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.block.core.AOTDShowBlockCreative
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register item event and registers all of our items
 */
class ItemRegister {
    /**
     * Called by forge to register any of our items
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>) {
        val registry = event.registry

        // Register each item in our item list
        registry.registerAll(*ModItems.ITEM_LIST)

        // For each block in our block list we register an item so that we can hold the block
        for (block in ModBlocks.BLOCK_LIST) {
            val properties = Item.Properties()
            if (block !is AOTDShowBlockCreative || block.displayInCreative()) {
                properties.tab(Constants.AOTD_CREATIVE_TAB)
            }
            registry.register(BlockItem(block, properties).setRegistryName(block.registryName))
        }
    }
}