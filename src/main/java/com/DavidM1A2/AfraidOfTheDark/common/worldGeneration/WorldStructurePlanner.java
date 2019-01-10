package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
			REGISTERED_STRUCTURES = new ArrayList<>(GameRegistry.findRegistry(Structure.class).getValuesCollection());

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
			StructurePlan structurePlan = StructurePlan.get(world);

			ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
			// If a structure does not yet exist at the position
			if (!structurePlan.structureExistsAt(chunkPos))
			{
				// Access the structures in random order
				Collections.shuffle(REGISTERED_STRUCTURES);

				// Grab the heightmap for the world
				IHeightmap heightmap = OverworldHeightmap.get(world);

				// Compute a random position for this structure with the bottom left corner in this chunk
				BlockPos chunk0Corner = chunkPos.getBlock(0, 63, 0);
				// Try placing each different structure at each possible permutation around with this chunk at the edge of the structure
				for (Structure structure : REGISTERED_STRUCTURES)
				{
					// Compute a list of possible positionings of this structure
					List<BlockPos> positions = new LinkedList<>();
					BlockPos basePos;

					int remainingWidth = structure.getXWidth();
					while(remainingWidth > 0)
					{
						// Iterate with the top of the structure intersecting this chunk
						basePos = chunk0Corner.add(15 - (structure.getXWidth() - remainingWidth), 0, 0);
						positions.add(basePos);
						positions.add(basePos.add(-world.rand.nextInt(MathHelper.clamp(remainingWidth, 0, 16)), 0, world.rand.nextInt(16)));

						// Iterate with the bottom of the structure intersecting this chunk
						basePos = chunk0Corner.add(15 - (structure.getXWidth() - remainingWidth), 0, 15 + structure.getZLength());
						positions.add(basePos);
						positions.add(basePos.add(-world.rand.nextInt(MathHelper.clamp(remainingWidth, 0, 16)), 0, -world.rand.nextInt(16)));

						remainingWidth = remainingWidth - 16;
					}

					int remainingHeight = structure.getZLength();
					while (remainingHeight > 0)
					{
						// Iterate with the left of the structure intersecting this chunk
						basePos = chunk0Corner.add(15, 0, 15 - (structure.getZLength() - remainingHeight));
						positions.add(basePos);
						positions.add(basePos.add(-world.rand.nextInt(16), 0, -world.rand.nextInt(MathHelper.clamp(remainingHeight, 0, 16))));

						// Iterate with the right of the structure intersecting this chunk
						basePos = chunk0Corner.add(0 - structure.getZLength(), 0, 15 - (structure.getZLength() - remainingHeight));
						positions.add(basePos);
						positions.add(basePos.add(world.rand.nextInt(16), 0, -world.rand.nextInt(MathHelper.clamp(remainingHeight, 0, 16))));

						remainingHeight = remainingHeight - 16;
					}

					// Each even index contains one extreme corner positioning, and each odd index contains a randomized permutation of that
					// positioning which we would actually generate at
					for (int i = 0; i < positions.size(); i = i + 2)
					{
						BlockPos possiblePos = positions.get(i);

						// Test if the structure fits into the structure map at the position (meaning no other structures would overlap this new structure)
						// and if the structure would fit based on the heightmap
						if (structurePlan.structureFitsAt(structure, possiblePos) && structure.canGenerateAt(possiblePos, heightmap))
						{
							// Place the structure into our structure plan
							structurePlan.placeStructureAt(structure, positions.get(i + 1));
							// We planned a structure for this chunk so return out
							return;
						}
					}
				}
			}
		}
	}
}
