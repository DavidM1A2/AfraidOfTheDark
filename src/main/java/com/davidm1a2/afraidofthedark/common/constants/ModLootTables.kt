package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.worldGeneration.LootTable
import net.minecraft.init.Items
import net.minecraft.util.ResourceLocation

/**
 * All mod loot tables used in afraid of the dark
 */
object ModLootTables {
    val CRYPT = LootTable(mapOf(null to ResourceLocation(Constants.MOD_ID, "crypt")))
    val WITCH_HUT = LootTable(mapOf(null to ResourceLocation(Constants.MOD_ID, "witch_hut")))
    val VOID_CHEST = LootTable(mapOf(null to ResourceLocation(Constants.MOD_ID, "void_chest")))
    val DARK_FOREST = LootTable(mapOf(null to ResourceLocation(Constants.MOD_ID, "dark_forest")))
    val NIGHTMARE_ISLAND = LootTable(
        mapOf(
            Items.COOKED_BEEF to ResourceLocation(Constants.MOD_ID, "nightmare/random_food"),
            Items.FEATHER to ResourceLocation(Constants.MOD_ID, "nightmare/insanity_research"),
            Items.SUGAR to ResourceLocation(Constants.MOD_ID, "nightmare/potions"),
            null to ResourceLocation(Constants.MOD_ID, "nightmare/random_blocks"),
            Items.DIAMOND to ResourceLocation(Constants.MOD_ID, "nightmare/valuable"),
            Items.BLAZE_ROD to ResourceLocation(Constants.MOD_ID, "nightmare/weaponry"),
            Items.EMERALD to ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part1"),
            Items.IRON_INGOT to ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part2"),
            Items.GOLD_INGOT to ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part3"),
            Items.NETHERBRICK to ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part4"),
            Items.COAL to ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part5"),
            Items.GOLD_NUGGET to ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part1"),
            Items.GLOWSTONE_DUST to ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part2"),
            Items.NETHER_WART to ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part3"),
            Items.RABBIT_HIDE to ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part4")
        )
    )
    val GNOMISH_CITY = LootTable(
        mapOf(
            null to ResourceLocation(Constants.MOD_ID, "gnomish_city/standard"),
            Items.DIAMOND to ResourceLocation(Constants.MOD_ID, "gnomish_city/rare")
        )
    )
}