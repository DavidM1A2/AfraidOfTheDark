package com.DavidM1A2.AfraidOfTheDark.common.reference;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicBlockReplacer;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicLoader;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.sizeof.RamUsageEstimator;

import net.minecraft.init.Blocks;

public enum AOTDSchematics
{
	TreeSmall("darkForest/TreeSmall.schematic"),
	TreeBranchyType1("darkForest/TreeBranchyType1.schematic"),
	TreeBranchyType2("darkForest/TreeBranchyType2.schematic"),
	TreeLargeCircle("darkForest/TreeLargeCircle.schematic"),
	TreeLargeDonut("darkForest/TreeLargeDonut.schematic"),
	BedHouse("darkForest/BedHouse.schematic"),
	PropBush1("darkForest/PropBush1.schematic"),
	PropFallenOverLog("darkForest/PropFallenOverLog.schematic"),
	PropFence1("darkForest/PropFence1.schematic"),
	PropFence2("darkForest/PropFence2.schematic"),
	PropFountain("darkForest/PropFountain.schematic"),
	PropLog("darkForest/PropLog.schematic"),
	PropPumpkin1("darkForest/PropPumpkin1.schematic"),
	PropPumpkin2("darkForest/PropPumpkin2.schematic"),
	PropStump("darkForest/PropStump.schematic"),

	Crypt("Crypt.schematic"),

	Spring("Spring.schematic"),

	NightmareIsland("NightmareIsland.schematic"),

	WitchHut("WitchHut.schematic"),

	VoidChestPortal("VoidChestPortal.schematic"),
	VoidChest("VoidChest.schematic"),

	TunnelEW("gnomishCity/TunnelEW.schematic"),
	TunnelNS("gnomishCity/TunnelNS.schematic"),
	RoomStairUp("gnomishCity/RoomStairUp.schematic"),
	RoomStairDown("gnomishCity/RoomStairDown.schematic"),
	RoomCave("gnomishCity/RoomCave.schematic"),
	RoomFarm("gnomishCity/RoomFarm.schematic"),
	RoomHotel("gnomishCity/RoomHotel.schematic"),
	RoomMeetingHall("gnomishCity/RoomMeetingHall.schematic"),
	RoomMushroom("gnomishCity/RoomMushroom.schematic"),
	RoomRuin("gnomishCity/RoomRuin.schematic"),
	RoomTanks("gnomishCity/RoomTanks.schematic"),
	Stairwell("gnomishCity/Stairwell.schematic"),
	EnariaLair("gnomishCity/EnariaLair.schematic");

	private static List<Schematic> rooms = new ArrayList<Schematic>();
	private Schematic schematic;

	static
	{
		rooms.add(RoomCave.getSchematic());
		rooms.add(RoomFarm.getSchematic());
		rooms.add(RoomHotel.getSchematic());
		rooms.add(RoomMeetingHall.getSchematic());
		rooms.add(RoomMushroom.getSchematic());
		rooms.add(RoomRuin.getSchematic());
		rooms.add(RoomTanks.getSchematic());

		SchematicBlockReplacer.replaceBlocks(TreeSmall.getSchematic(), Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
		SchematicBlockReplacer.replaceBlocks(TreeBranchyType1.getSchematic(), Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
		SchematicBlockReplacer.replaceBlocks(TreeBranchyType2.getSchematic(), Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
		SchematicBlockReplacer.replaceBlocks(TreeLargeCircle.getSchematic(), Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
		SchematicBlockReplacer.replaceBlocks(TreeLargeDonut.getSchematic(), Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
		SchematicBlockReplacer.replaceBlocks(BedHouse.getSchematic(), Blocks.lapis_block, ModBlocks.darkForest, Blocks.gold_block, Blocks.flower_pot, Blocks.iron_block, Blocks.cobblestone_wall, Blocks.gold_ore, Blocks.dark_oak_stairs, Blocks.bedrock, Blocks.spruce_stairs);

		SchematicBlockReplacer.replaceBlocks(Crypt.getSchematic(), Blocks.gold_block, ModBlocks.gravewoodStairs, Blocks.gold_ore, ModBlocks.gravewoodPlanks);
		SchematicBlockReplacer.replaceBlocks(Spring.getSchematic(), Blocks.gold_ore, ModBlocks.gravewoodLeaves, Blocks.gold_block, ModBlocks.spring);

		SchematicBlockReplacer.replaceBlocks(NightmareIsland.getSchematic(), Blocks.sponge, ModBlocks.enariaSpawner);

		SchematicBlockReplacer.replaceBlocks(VoidChestPortal.getSchematic(), Blocks.lapis_block, ModBlocks.eldritchStone);

		if (ConfigurationHandler.debugMessages)
		{
			long totalSize = 0;
			for (AOTDSchematics schematic : values())
				totalSize = totalSize + RamUsageEstimator.sizeOf(schematic.getSchematic());
			LogHelper.info("All loaded schematics are using this much ram: " + RamUsageEstimator.humanReadableUnits(totalSize));
		}
	}

	private AOTDSchematics(String name)
	{
		this.schematic = SchematicLoader.load(name);
	}

	public Schematic getSchematic()
	{
		return this.schematic;
	}

	public static List<Schematic> getGnomishCityRooms()
	{
		return rooms;
	}
}
