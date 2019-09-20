package com.davidm1a2.afraidofthedark.common.constants;

import com.davidm1a2.afraidofthedark.common.worldGeneration.LootTable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;

/**
 * All mod loot tables used in afraid of the dark
 */
public class ModLootTables
{
    public static final LootTable CRYPT = new LootTable(Collections.singletonMap(null, new ResourceLocation(Constants.MOD_ID, "crypt")));
    public static final LootTable WITCH_HUT = new LootTable(Collections.singletonMap(null, new ResourceLocation(Constants.MOD_ID, "witch_hut")));
    public static final LootTable VOID_CHEST = new LootTable(Collections.singletonMap(null, new ResourceLocation(Constants.MOD_ID, "void_chest")));
    public static final LootTable DARK_FOREST = new LootTable(Collections.singletonMap(null, new ResourceLocation(Constants.MOD_ID, "dark_forest")));
    public static final LootTable NIGHTMARE_ISLAND = new LootTable(new HashMap<Item, ResourceLocation>()
    {
        {
            put(Items.COOKED_BEEF, new ResourceLocation(Constants.MOD_ID, "nightmare/random_food"));
            put(Items.FEATHER, new ResourceLocation(Constants.MOD_ID, "nightmare/insanity_research"));
            put(Items.SUGAR, new ResourceLocation(Constants.MOD_ID, "nightmare/potions"));
            put(null, new ResourceLocation(Constants.MOD_ID, "nightmare/random_blocks"));
            put(Items.DIAMOND, new ResourceLocation(Constants.MOD_ID, "nightmare/valuable"));
            put(Items.BLAZE_ROD, new ResourceLocation(Constants.MOD_ID, "nightmare/weaponry"));
            put(Items.EMERALD, new ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part1"));
            put(Items.IRON_INGOT, new ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part2"));
            put(Items.GOLD_INGOT, new ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part3"));
            put(Items.NETHERBRICK, new ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part4"));
            put(Items.COAL, new ResourceLocation(Constants.MOD_ID, "nightmare/vitae1_part5"));
            put(Items.GOLD_NUGGET, new ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part1"));
            put(Items.GLOWSTONE_DUST, new ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part2"));
            put(Items.NETHER_WART, new ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part3"));
            put(Items.RABBIT_HIDE, new ResourceLocation(Constants.MOD_ID, "nightmare/astronomy2_part4"));
        }
    });
    public static final LootTable GNOMISH_CITY = new LootTable(new HashMap<Item, ResourceLocation>()
    {
        {
            put(null, new ResourceLocation(Constants.MOD_ID, "gnomish_city/standard"));
            put(Items.DIAMOND, new ResourceLocation(Constants.MOD_ID, "gnomish_city/rare"));
        }
    });
}
