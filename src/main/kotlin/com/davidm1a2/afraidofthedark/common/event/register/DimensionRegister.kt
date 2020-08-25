package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.ModDimension
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.world.RegisterDimensionsEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent

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
            DimensionType.byName(nightmare.registryName!!) ?: DimensionManager.registerDimension(nightmare.registryName, nightmare, null)

        val voidChest = ModDimensions.VOID_CHEST
        ModDimensions.VOID_CHEST_TYPE =
            DimensionType.byName(voidChest.registryName!!) ?: DimensionManager.registerDimension(voidChest.registryName, voidChest, null)
    }

    // TODO: Remove hack in 1.14.4
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    @Suppress("UNUSED_PARAMETER")
    fun onEntityJoinWorldEvent(event: FMLLoadCompleteEvent) {
        registerDimensions(RegisterDimensionsEvent(mapOf()))
    }

    // TODO: Remove hack in 1.14.4
    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    @Suppress("UNUSED_PARAMETER")
    fun initialization(event: FMLServerAboutToStartEvent) {
        registerDimensions(RegisterDimensionsEvent(mapOf()))
    }
}