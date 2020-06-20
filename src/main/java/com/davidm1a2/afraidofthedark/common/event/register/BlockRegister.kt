package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.Block
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register block event and registers all of our blocks
 */
class BlockRegister {
    /**
     * Called by forge to register any of our blocks
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerBlocks(event: RegistryEvent.Register<Block>) {
        val registry = event.registry

        // Register all blocks in our mod
        registry.registerAll(*ModBlocks.BLOCK_LIST)
    }
}