package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.CryptChestLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.DarkForestChestLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTable;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTableEntry;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.VoidChestLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.WitchHutLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity.GnomishCityLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity.GnomishCityRareLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part1;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part2;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part3;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part4;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.FoodLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.InsanityResearchLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.PotionLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.RandomBlockLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.ValueableLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part1Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part2Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part3Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part4Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part5Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.WeaponryLoot;

import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;

public enum AOTDLootTables
{
	Crypt(new LootTable(new LootTableEntry(new CryptChestLoot(), null, 1))),
	DarkForest(new LootTable(new LootTableEntry(new DarkForestChestLoot(), null, 6))),
	WitchHut(new LootTable(new LootTableEntry(new WitchHutLoot(), null, 1))),
	NightmareIsland(
			new LootTable(new LootTableEntry[] {
					new LootTableEntry(new FoodLoot(), Items.cooked_beef, 5 + MathHelper.floor_double(Math.random() * 5)),
					new LootTableEntry(new InsanityResearchLoot(), Items.feather, 20),
					new LootTableEntry(new PotionLoot(), Items.sugar, 5),
					new LootTableEntry(new RandomBlockLoot(), null, 12),
					new LootTableEntry(new ValueableLoot(), Items.diamond, 5),
					new LootTableEntry(new WeaponryLoot(), Items.blaze_rod, 4),
					new LootTableEntry(new Vitae1Part1Loot(), Items.emerald, 20),
					new LootTableEntry(new Vitae1Part2Loot(), Items.iron_ingot, 20),
					new LootTableEntry(new Vitae1Part3Loot(), Items.gold_ingot, 20),
					new LootTableEntry(new Vitae1Part4Loot(), Items.netherbrick, 20),
					new LootTableEntry(new Vitae1Part5Loot(), Items.coal, 20),
					new LootTableEntry(new Astronomy2Part1(), Items.gold_nugget, 20),
					new LootTableEntry(new Astronomy2Part2(), Items.glowstone_dust, 20),
					new LootTableEntry(new Astronomy2Part3(), Items.nether_wart, 20),
					new LootTableEntry(new Astronomy2Part4(), Items.rabbit_hide, 20), })),
	VoidChest(new LootTable(new LootTableEntry(new VoidChestLoot(), null, 3))),
	GnomishCity(new LootTable(new LootTableEntry(new GnomishCityLoot(), null, 1, 4), new LootTableEntry(new GnomishCityRareLoot(), Items.diamond, 1, 4)));

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
