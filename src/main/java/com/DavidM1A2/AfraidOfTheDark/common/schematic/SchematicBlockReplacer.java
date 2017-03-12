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
			put((short) -59, (short) Block.getIdFromBlock(Blocks.DARK_OAK_DOOR));
			put((short) -63, (short) Block.getIdFromBlock(Blocks.SPRUCE_DOOR));
			put((short) -64, (short) Block.getIdFromBlock(Blocks.ACACIA_FENCE));
			put((short) -65, (short) Block.getIdFromBlock(Blocks.DARK_OAK_FENCE));
			put((short) -67, (short) Block.getIdFromBlock(Blocks.BIRCH_FENCE));
			put((short) -68, (short) Block.getIdFromBlock(Blocks.SPRUCE_FENCE));
			put((short) -70, (short) Block.getIdFromBlock(Blocks.DARK_OAK_FENCE_GATE));
			put((short) -77, (short) Block.getIdFromBlock(Blocks.RED_SANDSTONE));
			put((short) -79, (short) Block.getIdFromBlock(Blocks.WALL_BANNER));
			put((short) -81, (short) Block.getIdFromBlock(Blocks.DOUBLE_PLANT));
			put((short) -83, (short) Block.getIdFromBlock(Blocks.COAL_BLOCK));
			put((short) -85, (short) Block.getIdFromBlock(Blocks.CARPET));
			put((short) -86, (short) Block.getIdFromBlock(Blocks.HAY_BLOCK));
			put((short) -87, (short) Block.getIdFromBlock(Blocks.SEA_LANTERN));
			put((short) -88, (short) Block.getIdFromBlock(Blocks.PRISMARINE));
			put((short) -90, (short) Block.getIdFromBlock(Blocks.BARRIER));
			put((short) -92, (short) Block.getIdFromBlock(Blocks.DARK_OAK_STAIRS));
			put((short) -93, (short) Block.getIdFromBlock(Blocks.ACACIA_STAIRS));
			put((short) -94, (short) Block.getIdFromBlock(Blocks.LOG2));
			put((short) -95, (short) Block.getIdFromBlock(Blocks.LEAVES));
			put((short) -96, (short) Block.getIdFromBlock(Blocks.STAINED_GLASS_PANE));
			put((short) -97, (short) Block.getIdFromBlock(Blocks.STAINED_HARDENED_CLAY));
			put((short) -100, (short) Block.getIdFromBlock(Blocks.QUARTZ_STAIRS));
			put((short) -102, (short) Block.getIdFromBlock(Blocks.HOPPER));
			put((short) -104, (short) Block.getIdFromBlock(Blocks.REDSTONE_BLOCK));
			put((short) -107, (short) Block.getIdFromBlock(Blocks.UNPOWERED_COMPARATOR));
			put((short) -108, (short) Block.getIdFromBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE));
			put((short) -109, (short) Block.getIdFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE));
			put((short) -110, (short) Block.getIdFromBlock(Blocks.TRAPPED_CHEST));
			put((short) -111, (short) Block.getIdFromBlock(Blocks.ANVIL));
			put((short) -112, (short) Block.getIdFromBlock(Blocks.SKULL));
			put((short) -114, (short) Block.getIdFromBlock(Blocks.POTATOES));
			put((short) -115, (short) Block.getIdFromBlock(Blocks.CARROTS));
			put((short) -116, (short) Block.getIdFromBlock(Blocks.FLOWER_POT));
			put((short) -117, (short) Block.getIdFromBlock(Blocks.COBBLESTONE_WALL));
			put((short) -119, (short) Block.getIdFromBlock(Blocks.COMMAND_BLOCK));
			put((short) -120, (short) Block.getIdFromBlock(Blocks.ACACIA_STAIRS));
			put((short) -121, (short) Block.getIdFromBlock(Blocks.BIRCH_STAIRS));
			put((short) -122, (short) Block.getIdFromBlock(Blocks.SPRUCE_STAIRS));
			put((short) -124, (short) Block.getIdFromBlock(Blocks.TRIPWIRE));
			put((short) -125, (short) Block.getIdFromBlock(Blocks.TRIPWIRE_HOOK));
			put((short) -127, (short) Block.getIdFromBlock(Blocks.GOLD_ORE));
			put((short) -128, (short) Block.getIdFromBlock(Blocks.SANDSTONE_STAIRS));
		}
	};

	private static final Map<Short, Short> knownModProblemIds = new HashMap<Short, Short>()
	{
		{
			put((short) 223, (short) Block.getIdFromBlock(ModBlocks.enariasAltar));
			put((short) 222, (short) Block.getIdFromBlock(ModBlocks.enariaSpawner));
			put((short) 221, (short) Block.getIdFromBlock(ModBlocks.glowStalk));
			put((short) 220, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut));
			put((short) 219, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate));
			put((short) 217, (short) Block.getIdFromBlock(ModBlocks.eldritchStone));
			put((short) 216, (short) Block.getIdFromBlock(ModBlocks.amorphousEldritchMetal));
			put((short) 215, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian));
			put((short) 214, (short) Block.getIdFromBlock(ModBlocks.voidChestPortal));
			put((short) 210, (short) Block.getIdFromBlock(ModBlocks.igneousBlock));
			put((short) 209, (short) Block.getIdFromBlock(ModBlocks.starMetalOre));
			put((short) 208, (short) Block.getIdFromBlock(ModBlocks.meteor));
			put((short) 204, (short) Block.getIdFromBlock(ModBlocks.gravewoodHalfSlab));
			put((short) 203, (short) Block.getIdFromBlock(ModBlocks.gravewoodStairs));
			put((short) 202, (short) Block.getIdFromBlock(ModBlocks.gravewoodPlanks));
			put((short) 201, (short) Block.getIdFromBlock(ModBlocks.gravewood));
			put((short) 200, (short) Block.getIdFromBlock(ModBlocks.gravewoodLeaves));
		}
	};

	public static Schematic replaceBlocks(Schematic schematic, Block... blocks)
	{
		if (blocks.length % 2 == 0)
			for (int i = 0; i < schematic.getBlocks().length; i++)
			{
				Block nextToPlace = Block.getBlockById(schematic.getBlocks()[i]);

				for (int j = 0; j < blocks.length; j = j + 2)
					if (nextToPlace == blocks[j])
						schematic.setBlock(blocks[j + 1], i);
			}

		return schematic;
	}

	public static void fixKnownSchematicErrors(Schematic schematic)
	{
		for (int i = 0; i < schematic.getBlocks().length; i++)
		{
			Short nextToPlace = schematic.getBlocks()[i];

			if (SchematicBlockReplacer.knownProblemIds.containsKey(nextToPlace))
				schematic.setBlock(SchematicBlockReplacer.knownProblemIds.get(nextToPlace), i);
			if (SchematicBlockReplacer.knownModProblemIds.containsKey(nextToPlace))
				schematic.setBlock(SchematicBlockReplacer.knownModProblemIds.get(nextToPlace), i);
		}
	}
}