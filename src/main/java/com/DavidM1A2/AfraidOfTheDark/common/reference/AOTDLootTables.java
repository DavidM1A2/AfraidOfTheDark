package com.DavidM1A2.AfraidOfTheDark.common.reference;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.LootTable;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.LootTableEntry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Items;

public enum AOTDLootTables
{
	Crypt(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "crypt"), null))),
	DarkForest(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "dark_forest"), null))),
	WitchHut(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "witch_hut"), null))),
	NightmareIsland(
			new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/random_food"), Items.COOKED_BEEF),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/insanity_research"), Items.FEATHER),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/potions"), Items.SUGAR),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/random_blocks"), null),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/valuable"), Items.DIAMOND),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/weaponry"), Items.BLAZE_ROD),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/vitae1_part1"), Items.EMERALD),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/vitae1_part2"), Items.IRON_INGOT),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/vitae1_part3"), Items.GOLD_INGOT),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/vitae1_part4"), Items.NETHERBRICK),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/vitae1_part5"), Items.COAL),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/astronomy2_part1"), Items.GOLD_NUGGET),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/astronomy2_part2"), Items.GLOWSTONE_DUST),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/astronomy2_part3"), Items.NETHER_WART),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "nightmare/astronomy2_part4"), Items.RABBIT_HIDE))),
	VoidChest(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "void_chest"), null))),
	GnomishCity(new LootTable(
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "gnomish_city/standard"), null),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "gnomish_city/rare"), Items.DIAMOND)));

	private LootTable lootTable;

	AOTDLootTables(LootTable lootTable)
	{
		this.lootTable = lootTable;
	}

	public LootTable getLootTable()
	{
		return this.lootTable;
	}
}
