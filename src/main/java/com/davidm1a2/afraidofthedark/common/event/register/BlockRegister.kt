package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks.BLOCK_LIST
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks.TILE_ENTITY_LIST
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Class that receives the register block event and registers all of our blocks
 */
class BlockRegister
{
    /**
     * Called by forge to register any of our blocks
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerBlocks(event: RegistryEvent.Register<Block>)
    {
        val registry = event.registry

        // Register all blocks in our mod
        registry.registerAll(*BLOCK_LIST)

        // Register any special tile entities
        for ((tileEntityClass, resourceLocation) in TILE_ENTITY_LIST)
        {
            GameRegistry.registerTileEntity(tileEntityClass, resourceLocation)
        }
    }

    /**
     * Called by forge to register any of our block renderers
     *
     * @param event The event that signifies that ModelLoader is ready to receive blocks
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registerBlockRenderers(event: ModelRegistryEvent)
    {
        // Register models for all blocks in our mod
        for (block in BLOCK_LIST)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, ModelResourceLocation(block.registryName!!, "inventory"))
        }
    }
}