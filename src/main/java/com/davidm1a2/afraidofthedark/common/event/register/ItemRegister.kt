package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlab
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemSlab
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.client.model.obj.OBJLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that receives the register item event and registers all of our items
 */
class ItemRegister
{
    /**
     * Called by forge to register any of our items
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>)
    {
        val registry = event.registry

        // Register each item in our item list
        registry.registerAll(*ModItems.ITEM_LIST)

        // For each block in our block list we register an item so that we can hold the block
        for (block in ModBlocks.BLOCK_LIST)
        {
            // If our block is a lower half slab, we register it using an ItemSlab not an ItemBlock
            if (block is AOTDSlab && !block.isDouble)
            {
                registry.register(ItemSlab(block, block, block.getOpposite()).setRegistryName(block.getRegistryName()))
            }
            else
            {
                registry.register(ItemBlock(block).setRegistryName(block.registryName))
            }
        }
    }

    /**
     * Called by forge to register any of our item renderers
     *
     * @param event The event that signifies that ModelLoader is ready to receive item
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registerItemRenderers(event: ModelRegistryEvent)
    {
        // Ensure any special models are registered
        OBJLoader.INSTANCE.addDomain(Constants.MOD_ID)

        // Register models for all items in our mod
        for (item in ModItems.ITEM_LIST)
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(item.registryName!!, "inventory"))
        }
    }
}