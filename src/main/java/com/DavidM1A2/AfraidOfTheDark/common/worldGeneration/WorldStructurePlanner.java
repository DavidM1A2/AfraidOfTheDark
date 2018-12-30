package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class used to map out the entire world structures used for structure generation
 */
public class WorldStructurePlanner
{
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
				// Create a random permutation of the structures to test, the first that passes the test will be generated
				List<Structure> registeredStructures = new ArrayList<>(GameRegistry.findRegistry(Structure.class).getValuesCollection());
				Collections.shuffle(registeredStructures);

				// Grab the heightmap for the world
				IHeightmap heightmap = OverworldHeightmap.get(world);

				// Compute a random position for this structure
				BlockPos structurePosition = chunkPos.getBlock(world.rand.nextInt(16), 63, world.rand.nextInt(16));

				// Try placing each different structure, once one works return
				for (Structure structure : registeredStructures)
				{
					// Test if the structure fits into the structure map at the position
					if (structurePlan.structureFitsAt(structure, structurePosition))
					{
						// Next test if the structure would fit based on the heightmap
						if (structure.canGenerateAt(structurePosition, heightmap))
						{
							// Place the structure and break out
							structurePlan.placeStructureAt(structure, structurePosition);
							break;
						}
					}
				}
			}
		}
	}
}
