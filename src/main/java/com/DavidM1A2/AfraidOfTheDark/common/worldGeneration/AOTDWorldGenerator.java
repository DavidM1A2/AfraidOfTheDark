package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Class that takes care of all world generation from afraid of the dark
 */
public class AOTDWorldGenerator implements IWorldGenerator
{
	/**
	 * Called to generate a chunk of the world
	 *
	 * @param random the chunk specific {@link Random}.
	 * @param chunkX the chunk X coordinate of this chunk.
	 * @param chunkZ the chunk Z coordinate of this chunk.
	 * @param world : additionalData[0] The minecraft {@link World} we're generating for.
	 * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
	 * @param chunkProvider : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		// Get the structure plan for the world
		StructurePlan structurePlan = StructurePlan.get(world);
		// Make sure that our plan is valid
		if (structurePlan != null)
		{
			// Create a chunk pos object for our chunk (X,Z) coords
			ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
			// Test if a structure exists at the X,Z coordinates
			if (structurePlan.structureExistsAt(chunkPos))
			{
				// The structure exists, grab it and the origin
				Structure structure = structurePlan.getStructureAt(chunkPos);
				BlockPos origin = structurePlan.getStructureOrigin(chunkPos);
				// If we want to show debug messages print a message that we're generating a piece of a structure
				if (AfraidOfTheDark.INSTANCE.getConfigurationHandler().showDebugMessages())
					AfraidOfTheDark.INSTANCE.getLogger().info("Structure " + structure.getRegistryName().toString() + " generated at " + origin.toString());
				// Generate the structure 
				structure.generate(world, origin, chunkPos);
			}
		}
	}
}
