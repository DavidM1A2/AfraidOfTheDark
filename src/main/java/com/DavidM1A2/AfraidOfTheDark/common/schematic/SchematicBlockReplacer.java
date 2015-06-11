package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import net.minecraft.block.Block;

public class SchematicBlockReplacer 
{
	public static Schematic replaceBlocks(Schematic schematic, Block... blocks)
	{
		if (blocks.length % 2 != 0)
		{
			return schematic;
		}
		
		int i = 0;
		
		for (int y = 0; y < schematic.getHeight(); y++)
		{
			for (int z = 0; z < schematic.getLength(); z++)
			{
				for (int x = 0; x < schematic.getWidth(); x++)
				{
					Block nextToPlace = Block.getBlockById(schematic.getBlocks()[i]);
					
					for (int j = 0; j < blocks.length; j = j + 2)
					{
						if (nextToPlace == blocks[j])
						{
							schematic.setBlock(blocks[j + 1], i);
						}
					}

					i = i + 1;
				}
			}
		}
		
		return schematic;
	}
}
