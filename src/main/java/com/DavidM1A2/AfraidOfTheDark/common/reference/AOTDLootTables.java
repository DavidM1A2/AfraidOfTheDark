package com.DavidM1A2.AfraidOfTheDark.common.reference;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTable;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTableEntry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Items;

public enum AOTDLootTables
{
	Crypt(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "crypt"), null))),
	DarkForest(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "dark_forest"), null))),
	WitchHut(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "witch_hut"), null))),
	NightmareIsland(
			new LootTable(new LootTableEntry[] {
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "FoodLoot"), Items.COOKED_BEEF),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "InsanityResearchLoot"), Items.FEATHER),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "PotionLoot"), Items.SUGAR),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "RandomBlockLoot"), null),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "ValueableLoot"), Items.DIAMOND),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "WeaponryLoot"), Items.BLAZE_ROD),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Vitae1Part1Loot"), Items.EMERALD),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Vitae1Part2Loot"), Items.IRON_INGOT),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Vitae1Part3Loot"), Items.GOLD_INGOT),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Vitae1Part4Loot"), Items.NETHERBRICK),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Vitae1Part5Loot"), Items.COAL),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Astronomy2Part1"), Items.GOLD_NUGGET),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Astronomy2Part2"), Items.GLOWSTONE_DUST),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Astronomy2Part3"), Items.NETHER_WART),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "Astronomy2Part4"), Items.RABBIT_HIDE), })),
	VoidChest(new LootTable(new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "crypt"), null))),
	GnomishCity(new LootTable(
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "GnomishCityLoot"), null),
					new LootTableEntry(new ResourceLocation(Reference.MOD_ID, "GnomishCityRareLoot"), Items.DIAMOND)));

	private LootTable lootTable;

	private AOTDLootTables(LootTable lootTable)
	{
		this.lootTable = lootTable;
	}

	public LootTable getLootTable()
	{
		return this.lootTable;
	}
}
