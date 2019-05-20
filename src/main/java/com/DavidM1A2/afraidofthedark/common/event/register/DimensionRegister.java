package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.dimension.nightmare.NightmareWorldProvider;
import com.DavidM1A2.afraidofthedark.common.dimension.voidChest.VoidChestWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.StringUtils;

/**
 * Class that registers all AOTD dimensions into the game
 */
public class DimensionRegister
{
    /**
     * Registers all AOTD dimensions
     */
    public static void initialize()
    {
        // The reason we can't do this in a loop is because the getNextFreeDimId() doesn't return a new value until after registerDimension is called, so we must call these lines in this order
        ModDimensions.VOID_CHEST = DimensionType.register("Void Chest", StringUtils.EMPTY, DimensionManager.getNextFreeDimId(), VoidChestWorldProvider.class, false);
        DimensionManager.registerDimension(ModDimensions.VOID_CHEST.getId(), ModDimensions.VOID_CHEST);
        ModDimensions.NIGHTMARE = DimensionType.register("Nightmare", StringUtils.EMPTY, DimensionManager.getNextFreeDimId(), NightmareWorldProvider.class, false);
        DimensionManager.registerDimension(ModDimensions.NIGHTMARE.getId(), ModDimensions.NIGHTMARE);
    }
}
