/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.util.HashMap;
import java.util.Map;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class SchematicBlockReplacer
{
	private static final Map<Short, Short> knownProblemIds = new HashMap<Short, Short>()
	{
		{
			put((short) -59, (short) Block.getIdFromBlock(Blocks.dark_oak_door));
			put((short) -63, (short) Block.getIdFromBlock(Blocks.spruce_door));
			put((short) -64, (short) Block.getIdFromBlock(Blocks.acacia_fence));
			put((short) -65, (short) Block.getIdFromBlock(Blocks.dark_oak_fence));
			put((short) -67, (short) Block.getIdFromBlock(Blocks.birch_fence));
			put((short) -68, (short) Block.getIdFromBlock(Blocks.spruce_fence));
			put((short) -70, (short) Block.getIdFromBlock(Blocks.dark_oak_fence_gate));
			put((short) -77, (short) Block.getIdFromBlock(Blocks.red_sandstone));
			put((short) -79, (short) Block.getIdFromBlock(Blocks.wall_banner));
			put((short) -81, (short) Block.getIdFromBlock(Blocks.double_plant));
			put((short) -83, (short) Block.getIdFromBlock(Blocks.coal_block));
			put((short) -85, (short) Block.getIdFromBlock(Blocks.carpet));
			put((short) -86, (short) Block.getIdFromBlock(Blocks.hay_block));
			put((short) -87, (short) Block.getIdFromBlock(Blocks.sea_lantern));
			put((short) -88, (short) Block.getIdFromBlock(Blocks.prismarine));
			put((short) -90, (short) Block.getIdFromBlock(Blocks.barrier));
			put((short) -92, (short) Block.getIdFromBlock(Blocks.dark_oak_stairs));
			put((short) -93, (short) Block.getIdFromBlock(Blocks.acacia_stairs));
			put((short) -94, (short) Block.getIdFromBlock(Blocks.log2));
			put((short) -95, (short) Block.getIdFromBlock(Blocks.leaves));
			put((short) -96, (short) Block.getIdFromBlock(Blocks.stained_glass_pane));
			put((short) -97, (short) Block.getIdFromBlock(Blocks.stained_hardened_clay));
			put((short) -100, (short) Block.getIdFromBlock(Blocks.quartz_stairs));
			put((short) -102, (short) Block.getIdFromBlock(Blocks.hopper));
			put((short) -104, (short) Block.getIdFromBlock(Blocks.redstone_block));
			put((short) -107, (short) Block.getIdFromBlock(Blocks.unpowered_comparator));
			put((short) -108, (short) Block.getIdFromBlock(Blocks.heavy_weighted_pressure_plate));
			put((short) -109, (short) Block.getIdFromBlock(Blocks.light_weighted_pressure_plate));
			put((short) -110, (short) Block.getIdFromBlock(Blocks.trapped_chest));
			put((short) -111, (short) Block.getIdFromBlock(Blocks.anvil));
			put((short) -112, (short) Block.getIdFromBlock(Blocks.skull));
			put((short) -114, (short) Block.getIdFromBlock(Blocks.potatoes));
			put((short) -115, (short) Block.getIdFromBlock(Blocks.carrots));
			put((short) -116, (short) Block.getIdFromBlock(Blocks.flower_pot));
			put((short) -117, (short) Block.getIdFromBlock(Blocks.cobblestone_wall));
			put((short) -119, (short) Block.getIdFromBlock(Blocks.command_block));
			put((short) -120, (short) Block.getIdFromBlock(Blocks.acacia_stairs));
			put((short) -121, (short) Block.getIdFromBlock(Blocks.birch_stairs));
			put((short) -122, (short) Block.getIdFromBlock(Blocks.spruce_stairs));
			put((short) -124, (short) Block.getIdFromBlock(Blocks.tripwire));
			put((short) -125, (short) Block.getIdFromBlock(Blocks.tripwire_hook));
			put((short) -127, (short) Block.getIdFromBlock(Blocks.gold_ore));
			put((short) -128, (short) Block.getIdFromBlock(Blocks.sandstone_stairs));
		}
	};

	private static final Map<Short, Short> knownModProblemIds = new HashMap<Short, Short>()
	{
		{
			put((short) -34, (short) Block.getIdFromBlock(ModBlocks.enariaSpawner));
			put((short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk));
			put((short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut));
			put((short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate));
			put((short) -39, (short) Block.getIdFromBlock(ModBlocks.eldritchStone));
			put((short) -40, (short) Block.getIdFromBlock(ModBlocks.amorphousEldritchMetal));
			put((short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian));
			put((short) -42, (short) Block.getIdFromBlock(ModBlocks.voidChestPortal));
			put((short) -46, (short) Block.getIdFromBlock(ModBlocks.igneousBlock));
			put((short) -47, (short) Block.getIdFromBlock(ModBlocks.starMetalOre));
			put((short) -48, (short) Block.getIdFromBlock(ModBlocks.meteor));
			put((short) -52, (short) Block.getIdFromBlock(ModBlocks.gravewoodHalfSlab));
			put((short) -53, (short) Block.getIdFromBlock(ModBlocks.gravewoodStairs));
			put((short) -54, (short) Block.getIdFromBlock(ModBlocks.gravewoodPlanks));
			put((short) -55, (short) Block.getIdFromBlock(ModBlocks.gravewood));
			put((short) -56, (short) Block.getIdFromBlock(ModBlocks.gravewoodLeaves));
		}
	};

	public static Schematic replaceBlocks(Schematic schematic, Block... blocks)
	{
		if (blocks.length % 2 != 0)
		{
			return schematic;
		}

		for (int i = 0; i < schematic.getBlocks().length; i++)
		{
			Block nextToPlace = Block.getBlockById(schematic.getBlocks()[i]);

			for (int j = 0; j < blocks.length; j = j + 2)
			{
				if (nextToPlace == blocks[j])
				{
					schematic.setBlock(blocks[j + 1], i);
				}
			}
		}

		return schematic;
	}

	public static Schematic replaceBlocks(Schematic schematic, Short... blocks)
	{
		if (blocks.length % 2 != 0)
		{
			return schematic;
		}

		for (int i = 0; i < schematic.getBlocks().length; i++)
		{
			Short nextToPlace = schematic.getBlocks()[i];

			for (int j = 0; j < blocks.length; j = j + 2)
			{
				if (nextToPlace == blocks[j])
				{
					schematic.setBlock(blocks[j + 1], i);
				}
			}
		}

		return schematic;
	}

	public static void fixKnownSchematicErrors(Schematic schematic)
	{
		for (int i = 0; i < schematic.getBlocks().length; i++)
		{
			Short nextToPlace = schematic.getBlocks()[i];

			if (SchematicBlockReplacer.knownProblemIds.containsKey(nextToPlace))
			{
				schematic.setBlock(SchematicBlockReplacer.knownProblemIds.get(nextToPlace), i);
			}

			if (SchematicBlockReplacer.knownModProblemIds.containsKey(nextToPlace))
			{
				schematic.setBlock(SchematicBlockReplacer.knownModProblemIds.get(nextToPlace), i);
			}
		}
	}
}