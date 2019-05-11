package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Class that registers all furnace recipes
 */
public class FurnaceRecipeRegister
{
    /**
     * Furnace recipes don't initialize through Json, instead you do that with code which we just trigger in the constructor
     */
    public static void initialize()
    {
        // Register smelting recipe for astral silver ore -> ingot
        GameRegistry.addSmelting(ModBlocks.ASTRAL_SILVER_ORE, new ItemStack(ModItems.ASTRAL_SILVER_INGOT), 0.4f);
        GameRegistry.addSmelting(ModItems.STAR_METAL_FRAGMENT, new ItemStack(ModItems.STAR_METAL_INGOT), 0.5f);
    }
}
