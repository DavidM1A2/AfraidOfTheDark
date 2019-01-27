package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.LootTable;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * All mod loot tables used in afraid of the dark
 */
public class ModLootTables
{
	public static final LootTable CRYPT = new LootTable(new HashMap<Item, ResourceLocation>()
	{{
		put(null, new ResourceLocation(Constants.MOD_ID, "crypt"));
	}});
	public static final LootTable WITCH_HUT = new LootTable(new HashMap<Item, ResourceLocation>()
	{{
		put(null, new ResourceLocation(Constants.MOD_ID, "witch_hut"));
	}});
}
