package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.loottable.LootTable
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation

/**
 * All mod loot tables used in afraid of the dark
 */
object ModLootTables {
    val CRYPT = LootTable("crypt", mapOf(null to ResourceLocation(Constants.MOD_ID, "chests/crypt")))
    val WITCH_HUT = LootTable("witch_hut", mapOf(null to ResourceLocation(Constants.MOD_ID, "chests/witch_hut")))
    val VOID_CHEST = LootTable("void_chest", mapOf(null to ResourceLocation(Constants.MOD_ID, "chests/void_chest")))
    val DARK_FOREST = LootTable("dark_forest", mapOf(null to ResourceLocation(Constants.MOD_ID, "chests/dark_forest")))
    val OBSERVATORY = LootTable("observatory", mapOf(null to ResourceLocation(Constants.MOD_ID, "chests/observatory")))
    val NIGHTMARE_ISLAND = LootTable(
        "nightmare_island",
        mapOf(
            Items.COOKED_BEEF to ResourceLocation(Constants.MOD_ID, "chests/nightmare/random_food"),
            Items.FEATHER to ResourceLocation(Constants.MOD_ID, "chests/nightmare/insanity_research"),
            Items.SUGAR to ResourceLocation(Constants.MOD_ID, "chests/nightmare/potions"),
            null to ResourceLocation(Constants.MOD_ID, "chests/nightmare/random_blocks"),
            Items.DIAMOND to ResourceLocation(Constants.MOD_ID, "chests/nightmare/valuable"),
            Items.BLAZE_ROD to ResourceLocation(Constants.MOD_ID, "chests/nightmare/weaponry"),
            Items.EMERALD to ResourceLocation(Constants.MOD_ID, "chests/nightmare/vitae_lantern_part1"),
            Items.IRON_INGOT to ResourceLocation(Constants.MOD_ID, "chests/nightmare/vitae_lantern_part2"),
            Items.GOLD_INGOT to ResourceLocation(Constants.MOD_ID, "chests/nightmare/vitae_lantern_part3"),
            Items.NETHER_BRICK to ResourceLocation(Constants.MOD_ID, "chests/nightmare/vitae_lantern_part4"),
            Items.COAL to ResourceLocation(Constants.MOD_ID, "chests/nightmare/vitae_lantern_part5"),
            Items.GOLD_NUGGET to ResourceLocation(Constants.MOD_ID, "chests/nightmare/astronomy2_part1"),
            Items.GLOWSTONE_DUST to ResourceLocation(Constants.MOD_ID, "chests/nightmare/astronomy2_part2"),
            Items.NETHER_WART to ResourceLocation(Constants.MOD_ID, "chests/nightmare/astronomy2_part3"),
            Items.RABBIT_HIDE to ResourceLocation(Constants.MOD_ID, "chests/nightmare/astronomy2_part4")
        )
    )
    val FORBIDDEN_CITY = LootTable(
        "forbidden_city",
        mapOf(
            null to ResourceLocation(Constants.MOD_ID, "chests/forbidden_city/standard"),
            Items.GOLD_INGOT to ResourceLocation(Constants.MOD_ID, "chests/forbidden_city/cave_room"),
            Items.DIAMOND to ResourceLocation(Constants.MOD_ID, "chests/forbidden_city/rare")
        )
    )
    val DESERT_OASIS = LootTable(
        "desert_oasis",
        mapOf(
            ModBlocks.GRAVEWOOD_SAPLING to ResourceLocation(
                Constants.MOD_ID,
                "chests/desert_oasis/low_tier"
            ),
            ModBlocks.MANGROVE_SAPLING to ResourceLocation(
                Constants.MOD_ID,
                "chests/desert_oasis/mid_tier"
            ),
            ModBlocks.SACRED_MANGROVE_SAPLING to ResourceLocation(
                Constants.MOD_ID,
                "chests/desert_oasis/high_tier"
            )
        )
    )
    val FROST_PHOENIX_PERCH_SMALL = LootTable(
        "frost_phoenix_perch_small",
        mapOf(
            null to ResourceLocation(Constants.MOD_ID, "chests/frost_phoenix_perch_small/standard"),
            Items.DIAMOND to ResourceLocation(Constants.MOD_ID, "chests/frost_phoenix_perch_small/rare")
        )
    )

    private val LOOT_TABLES = listOf(
        CRYPT,
        WITCH_HUT,
        VOID_CHEST,
        DARK_FOREST,
        OBSERVATORY,
        NIGHTMARE_ISLAND,
        FORBIDDEN_CITY,
        DESERT_OASIS,
    )

    val NAME_TO_LOOT_TABLE = LOOT_TABLES.associateBy { it.name }
}