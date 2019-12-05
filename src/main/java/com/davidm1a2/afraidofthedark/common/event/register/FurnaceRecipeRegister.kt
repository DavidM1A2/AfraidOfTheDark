package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Class that registers all furnace recipes
 */
object FurnaceRecipeRegister
{
    /**
     * Furnace recipes don't initialize through Json, instead you do that with code which we just trigger here
     */
    fun initialize()
    {
        // Register smelting recipe for astral silver ore -> ingot
        GameRegistry.addSmelting(ModBlocks.ASTRAL_SILVER_ORE, ItemStack(ModItems.ASTRAL_SILVER_INGOT), 0.4f)
        // Star metal fragment -> star metal ingot
        GameRegistry.addSmelting(ModItems.STAR_METAL_FRAGMENT, ItemStack(ModItems.STAR_METAL_INGOT), 0.5f)
        // Gravewood -> charcoal
        GameRegistry.addSmelting(ItemStack(ModBlocks.GRAVEWOOD), ItemStack(Items.COAL, 1, 1), 0.15f)
        // Mangrove -> charcoal
        GameRegistry.addSmelting(ItemStack(ModBlocks.MANGROVE), ItemStack(Items.COAL, 1, 1), 0.15f)
    }
}