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
    fun registerDimensions(event: RegisterDimensionsEvent) {
        val nightmare = ModDimensions.NIGHTMARE
        if (DimensionType.byName(nightmare.registryName!!) == null) {
            ModDimensions.NIGHTMARE_TYPE = DimensionManager.registerDimension(nightmare.registryName, nightmare, null)
        }

        val voidChest = ModDimensions.VOID_CHEST
        if (DimensionType.byName(voidChest.registryName!!) == null) {
            ModDimensions.VOID_CHEST_TYPE = DimensionManager.registerDimension(voidChest.registryName, voidChest, null)
        }
    }

    /**
     * Registers all AOTD dimensions
     */
    /*
fun initialize() {
    var voidChestDimensionId = configurationHandler.voidChestDimensionId
    if (voidChestDimensionId == 0) {
        voidChestDimensionId = DimensionManager.getNextFreeDimId()
    }
    ModDimensions.VOID_CHEST_TYPE = DimensionType.register(
        "Void Chest",
        StringUtils.EMPTY,
        voidChestDimensionId,
        VoidChestDimension::class.java,
        false
    )
    DimensionManager.registerDimension(ModDimensions.VOID_CHEST_TYPE.id, ModDimensions.VOID_CHEST_TYPE)

    var nightmareDimensionId = configurationHandler.nightmareDimensionId
    if (nightmareDimensionId == 0) {
        nightmareDimensionId = DimensionManager.getNextFreeDimId()
    }
    ModDimensions.NIGHTMARE_TYPE = DimensionType.register(
        "Nightmare",
        StringUtils.EMPTY,
        nightmareDimensionId,
        NightmareDimension::class.java,
        false
    )
    DimensionManager.registerDimension(ModDimensions.NIGHTMARE_TYPE.id, ModDimensions.NIGHTMARE_TYPE)
}

     */
}