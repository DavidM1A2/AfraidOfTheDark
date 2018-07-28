package com.DavidM1A2.AfraidOfTheDark.common.reference;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicLoader;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.sizeof.RamUsageEstimator;

public enum AOTDSchematics
{
	TreeSmall("dark_forest/tree_small.schematic"),
	TreeBranchyType1("dark_forest/tree_branchy_type_1.schematic"),
	TreeBranchyType2("dark_forest/tree_branchy_type_2.schematic"),
	TreeLargeCircle("dark_forest/tree_large_circle.schematic"),
	TreeLargeDonut("dark_forest/tree_large_donut.schematic"),
	BedHouse("dark_forest/bed_house.schematic"),
	PropBush1("dark_forest/prop_bush_1.schematic"),
	PropFallenOverLog("dark_forest/prop_fallen_over_log.schematic"),
	PropFence1("dark_forest/prop_fence_1.schematic"),
	PropFence2("dark_forest/prop_fence_2.schematic"),
	PropFountain("dark_forest/prop_fountain.schematic"),
	PropLog("dark_forest/prop_log.schematic"),
	PropPumpkin1("dark_forest/prop_pumpkin_1.schematic"),
	PropPumpkin2("dark_forest/prop_pumpkin_2.schematic"),
	PropStump("dark_forest/prop_stump.schematic"),

	Crypt("crypt.schematic"),

	Spring("spring.schematic"),

	NightmareIsland("nightmare_island.schematic"),

	EnariasAltar("enarias_altar.schematic"),

	WitchHut("witch_hut.schematic"),

	VoidChestPortal("void_chest_portal.schematic"),
	VoidChest("void_chest.schematic"),

	TunnelEW("gnomish_city/tunnel_ew.schematic"),
	TunnelNS("gnomish_city/tunnel_ns.schematic"),
	RoomStairUp("gnomish_city/room_stair_up.schematic"),
	RoomStairDown("gnomish_city/room_stair_down.schematic"),
	RoomCave("gnomish_city/room_cave.schematic"),
	RoomFarm("gnomish_city/room_farm.schematic"),
	RoomHotel("gnomish_city/room_hotel.schematic"),
	RoomMeetingHall("gnomish_city/room_meeting_hall.schematic"),
	RoomMushroom("gnomish_city/room_mushroom.schematic"),
	RoomRuin("gnomish_city/room_ruin.schematic"),
	RoomTanks("gnomish_city/room_tanks.schematic"),
	Stairwell("gnomish_city/stairwell.schematic"),
	EnariaLair("gnomish_city/enaria_lair.schematic");

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
