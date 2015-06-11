/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class SchematicGenerator 
{
	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic == null)
		{
			return;
		}
		
		int i = 0;
		
		for (int y = 0; y < schematic.getHeight(); y++)
		{
			for (int z = 0; z < schematic.getLength(); z++)
			{
				for (int x = 0; x < schematic.getWidth(); x++)
				{
					Block nextToPlace = Block.getBlockById(schematic.getBlocks()[i]);
					if (nextToPlace != Blocks.air)
					{
						BlockPos currentLocation = new BlockPos(x + xPosition, y + yPosition, z + zPosition);
						world.setBlockToAir(new BlockPos(currentLocation));
						world.setBlockState(currentLocation, nextToPlace.getStateFromMeta(schematic.getData()[i]));
					}
					
					i = i + 1;
				}
			}
		}
	}
}
