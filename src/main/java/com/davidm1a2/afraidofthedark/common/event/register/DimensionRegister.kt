package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.dimension.nightmare.NightmareWorldProvider
import com.davidm1a2.afraidofthedark.common.dimension.voidChest.VoidChestWorldProvider
import net.minecraft.world.DimensionType
import net.minecraftforge.common.DimensionManager
import org.apache.commons.lang3.StringUtils

/**
 * Class that registers all AOTD dimensions into the game
 */
object DimensionRegister {
    /**
     * Registers all AOTD dimensions
     */
    fun initialize() {
        // The reason we can't do this in a loop is because the getNextFreeDimId() doesn't return a new value until after registerDimension is called, so we must call these lines in this order
        val configurationHandler = AfraidOfTheDark.INSTANCE.configurationHandler

        // For each dimension:
        // 1) Grab the ID from config
        // 2) If it's the key ID of 0 then grab a dynamic ID instead
        // 3) Register the dimension type with the ID
        // 4) Instantly register the dimension after, incrementing the next free ID for the next mod

        var voidChestDimensionId = configurationHandler.voidChestDimensionId
        if (voidChestDimensionId == 0) {
            voidChestDimensionId = DimensionManager.getNextFreeDimId()
        }
        ModDimensions.VOID_CHEST = DimensionType.register(
            "Void Chest",
            StringUtils.EMPTY,
            voidChestDimensionId,
            VoidChestWorldProvider::class.java,
            false
        )
        DimensionManager.registerDimension(ModDimensions.VOID_CHEST.id, ModDimensions.VOID_CHEST)

        var nightmareDimensionId = configurationHandler.nightmareDimensionId
        if (nightmareDimensionId == 0) {
            nightmareDimensionId = DimensionManager.getNextFreeDimId()
        }
        ModDimensions.NIGHTMARE = DimensionType.register(
            "Nightmare",
            StringUtils.EMPTY,
            nightmareDimensionId,
            NightmareWorldProvider::class.java,
            false
        )
        DimensionManager.registerDimension(ModDimensions.NIGHTMARE.id, ModDimensions.NIGHTMARE)
    }
}