package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.IStructurePlan;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class used to map out the entire world structures used for structure generation
 */
public class WorldStructurePlanner
{
	// Create a random permutation of the structures to test, the first that passes the test will be generated
	private List<Structure> REGISTERED_STRUCTURES = null;

	/**
	 * Called whenever a chunk is generated and needs population, we update our terrain generation here
	 *
	 * @param event The event containing the chunk and world
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onChunkPopulated(PopulateChunkEvent.Pre event)
	{
		// Get a reference to the world
		World world = event.getWorld();
		// Get the X and Z coordinates of the chunk that was populated
		int chunkX = event.getChunkX();
		int chunkZ = event.getChunkZ();

		// If we do not know of our structure list yet grab the list from the registry
		if (REGISTERED_STRUCTURES == null)
			REGISTERED_STRUCTURES = new ArrayList<>(ModRegistries.STRUCTURE.getValuesCollection());

		// Generate the structure plan for the chunk
		this.planStructuresInChunk(world, chunkX, chunkZ);
	}

	/**
	 * Called to generate the plan of structures for a chunk
	 *
	 * @param world The world to plan structures in, should be server side
	 * @param chunkX The X coordinate of the chunk
	 * @param chunkZ The Z coordinate of the chunk
	 */
	private void planStructuresInChunk(World world, int chunkX, int chunkZ)
	{
		// If the world is server side, and it's the overworld, we begin planning overworld structures
		if (!world.isRemote && world.provider.getDimension() == 0)
		{
			// Grab the structure plan for the world
			IStructurePlan structurePlan = StructurePlan.get(world);

			ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
			// If a structure does not yet exist at the position
			if (!structurePlan.structureExistsAt(chunkPos))
			{
				// Grab a reference to the random object
				Random random = world.rand;

				// Access the structures in random order
				Collections.shuffle(REGISTERED_STRUCTURES, random);

				// Grab the heightmap for the world
				IHeightmap heightmap = OverworldHeightmap.get(world);

				// Grab the biome provider for the world which we use to test which biomes exist where
				BiomeProvider biomeProvider = world.getBiomeProvider();

				// Compute the 0,0 block pos for this structure
				BlockPos chunk0Corner = chunkPos.getBlock(0, 63, 0);

				// Compute the 4 possible positions of this structure with 4 extra slots for randomized versions of that position
				BlockPos[] positions = new BlockPos[8];

				// Try placing each different structure at each possible permutation around with this chunk at the edge of the structure
				for (Structure structure : REGISTERED_STRUCTURES)
				{
					// Here we use a heuristic to test if a structure will fit inside of a chunk, we only test if we place the corners of the chunk
					// inside the chunkpos instead of all possible positionings of the structure over the chunk

					// Position the structure in the top right of the chunk
					BlockPos topRight = chunk0Corner.add(15, 0, 15);
					positions[0] = topRight;
					positions[1] = topRight.add(-random.nextInt(16), 0, -random.nextInt(16));

					// Position the structure in the top left of the chunk
					BlockPos topLeft = chunk0Corner.add(0 - structure.getXWidth() + 1, 0, 15);
					positions[2] = topLeft;
					positions[3] = topLeft.add(random.nextInt(16), 0, -random.nextInt(16));

					// Position the structure in the bottom left of the chunk
					BlockPos bottomLeft = chunk0Corner.add(0 - structure.getXWidth() + 1, 0, 0 - structure.getZLength() + 1);
					positions[4] = bottomLeft;
					positions[5] = bottomLeft.add(random.nextInt(16), 0, random.nextInt(16));

					// Position the structure in the bottom right of the chunk
					BlockPos bottomRight = chunk0Corner.add(15, 0, 0 - structure.getZLength() + 1);
					positions[6] = bottomRight;
					positions[7] = bottomRight.add(-random.nextInt(16), 0, random.nextInt(16));

					// Each even index contains one extreme corner positioning, and each odd index contains a randomized permutation of that
					// positioning which we would actually generate at
					for (int i = 0; i < positions.length; i = i + 2)
					{
						BlockPos possiblePos = positions[i];

						// Test if the structure fits into the structure map at the position (meaning no other structures would overlap this new structure)
						// and if the structure would fit based on the heightmap
						if (structurePlan.structureFitsAt(structure, possiblePos))
						{
							// Compute the chance that the structure could spawn here
							double percentChance = structure.computeChanceToGenerateAt(possiblePos, heightmap, biomeProvider);
							// If our random dice roll succeeds place the structure
							if (random.nextDouble() < percentChance)
							{
								// Grab the randomized position to generate the structure at
								BlockPos posToGenerate = positions[i + 1];

								// Place the structure into our structure plan
								structurePlan.placeStructureAt(structure, posToGenerate, structure.generateStructureData());

								// Generate any chunks that this structure will generate in that are already generated
								this.generateExistingChunks(structure, world, posToGenerate);

								// We planned a structure for this chunk so return out
								return;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * When we plan a structure it may overlap existing chunks so we need to generate the structure in those chunks
	 *
	 * @param structure The structure to generate
	 * @param world The world to generate in
	 * @param posToGenerate The position to generate at
	 */
	private void generateExistingChunks(Structure structure, World world, BlockPos posToGenerate)
	{
		// Store a reference to the world generator
		AOTDWorldGenerator worldGenerator = AfraidOfTheDark.INSTANCE.getWorldGenerator();

		// Compute the bottom left and top right chunk position
		ChunkPos bottomLeftCorner = new ChunkPos(posToGenerate);
		ChunkPos topRightCorner = new ChunkPos(posToGenerate.add(structure.getXWidth(), 0, structure.getZLength()));

		// Iterate over all chunks in the region and test if the world has a given chunk or not yet
		for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
			for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
				// If the chunk is generated at the coordinates then generate the structure at that position
				if (world.isChunkGeneratedAt(chunkX, chunkZ))
					worldGenerator.addChunkToRegenerate(new ChunkPos(chunkX, chunkZ));
	}
}
