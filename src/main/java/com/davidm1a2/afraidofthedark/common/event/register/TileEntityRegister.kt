package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register tile entity event and registers all of our tile entities
 */
class TileEntityRegister {
    /**
     * Called by forge to register any of our tile entities
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerTileEntities(event: RegistryEvent.Register<TileEntityType<*>>) {
        val registry = event.registry

        // Register all blocks in our mod
        registry.registerAll(*ModTileEntities.TILE_ENTITY_LIST)
    }
}