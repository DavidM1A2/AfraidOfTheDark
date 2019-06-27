package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.dimension.nightmare.NightmareWorldProvider;
import com.DavidM1A2.afraidofthedark.common.dimension.voidChest.VoidChestWorldProvider;
import com.DavidM1A2.afraidofthedark.common.event.ConfigurationHandler;
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

        ConfigurationHandler configurationHandler = AfraidOfTheDark.INSTANCE.getConfigurationHandler();

        // For each dimension:
        // 1) Grab the ID from config
        // 2) If it's the key ID of 0 then grab a dynamic ID instead
        // 3) Register the dimension type with the ID
        // 4) Instantly register the dimension after, incrementing the next free ID for the next mod

        int voidChestDimensionId = configurationHandler.getVoidChestDimensionId();
        if (voidChestDimensionId == 0)
        {
            voidChestDimensionId = DimensionManager.getNextFreeDimId();
        }
        ModDimensions.VOID_CHEST = DimensionType.register("Void Chest", StringUtils.EMPTY, voidChestDimensionId, VoidChestWorldProvider.class, false);
        DimensionManager.registerDimension(ModDimensions.VOID_CHEST.getId(), ModDimensions.VOID_CHEST);

        int nightmareDimensionId = configurationHandler.getNightmareDimensionId();
        if (nightmareDimensionId == 0)
        {
            nightmareDimensionId = DimensionManager.getNextFreeDimId();
        }
        ModDimensions.NIGHTMARE = DimensionType.register("Nightmare", StringUtils.EMPTY, nightmareDimensionId, NightmareWorldProvider.class, false);
        DimensionManager.registerDimension(ModDimensions.NIGHTMARE.getId(), ModDimensions.NIGHTMARE);
    }
}
