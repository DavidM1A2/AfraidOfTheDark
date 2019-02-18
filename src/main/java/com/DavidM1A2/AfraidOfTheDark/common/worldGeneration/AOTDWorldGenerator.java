package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class that takes care of all world generation from afraid of the dark
 */
public class AOTDWorldGenerator implements IWorldGenerator
{
	// A queue of chunk positions that need to be generated
	private final Queue<ChunkPos> chunksThatNeedGeneration = new ConcurrentLinkedQueue<>();

	/**
	 * Adds a chunk position that needs regeneration
	 *
	 * @param chunkPos The position to regenerate
	 */
	void addChunkToRegenerate(ChunkPos chunkPos)
	{
		this.chunksThatNeedGeneration.add(chunkPos);
	}

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
		// Create a chunk pos object for our chunk (X,Z) coords
		ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
		// Generate the chunk
		this.generate(world, chunkPos);
	}

	/**
	 * Tick handler tests if there is any structures awaiting generation and if so it consumes the structure and generates it. This is needed
	 * because there's a chance that we place a structure in already generated chunks
	 *
	 * @param event The tick event
	 */
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		// We can assume event.type == Type.SERVER and event.side == Side.SERVER because we are listening to the ServerTickEvent
		// Ensure phase is tick start to perform the generation
		if (event.phase == TickEvent.Phase.START)
		{
			// Test if we have a chunk to generate
			if (!this.chunksThatNeedGeneration.isEmpty())
			{
				// Grab the chunk to generate
				ChunkPos chunkToGenerate = this.chunksThatNeedGeneration.remove();
				// Grab the world to generate which is the overworld (world 0)
				World world = DimensionManager.getWorld(0);
				// Generate the chunk
				this.generate(world, chunkToGenerate);
			}
		}
	}

	/**
	 * Generates AOTD structures in a given world at the given position
	 *
	 * @param world The world to generate in
	 * @param chunkPos The position to generate at
	 */
	private void generate(World world, ChunkPos chunkPos)
	{
		// Get the structure plan for the world
		StructurePlan structurePlan = StructurePlan.get(world);
		// Make sure that our plan is valid
		if (structurePlan != null)
		{
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
