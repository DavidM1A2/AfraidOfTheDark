package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.LootTable;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class ModLootTables
{
	public static final LootTable CRYPT = new LootTable(new HashMap<Item, ResourceLocation>()
	{{
		put(null, new ResourceLocation(Constants.MOD_ID, "crypt"));
	}});
}
