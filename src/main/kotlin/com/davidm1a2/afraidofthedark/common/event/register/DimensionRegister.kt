package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.ModDimension
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.world.RegisterDimensionsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that registers all AOTD dimensions into the game
 */
class DimensionRegister {
    /**
     * Called by forge to register any of our mod dimension references
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerModDimensions(event: RegistryEvent.Register<ModDimension>) {
        event.registry.registerAll(*ModDimensions.DIMENSION_LIST)
    }

    /**
     * Called by forge to register any of our mod dimensions. This is required since dimensions
     * are are created in 2 parts
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registerDimensions(event: RegisterDimensionsEvent) {
        val nightmare = ModDimensions.NIGHTMARE
        ModDimensions.NIGHTMARE_TYPE =
            DimensionType.byName(nightmare.registryName!!) ?: DimensionManager.registerDimension(nightmare.registryName, nightmare, null, false)

        val voidChest = ModDimensions.VOID_CHEST
        ModDimensions.VOID_CHEST_TYPE =
            DimensionType.byName(voidChest.registryName!!) ?: DimensionManager.registerDimension(voidChest.registryName, voidChest, null, false)
    }
}