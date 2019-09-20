package com.davidm1a2.afraidofthedark.common.worldGeneration;

import com.davidm1a2.afraidofthedark.common.capabilities.world.IStructurePlan;
import com.davidm1a2.afraidofthedark.common.capabilities.world.PlacedStructure;
import com.davidm1a2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
     * @param random         the chunk specific {@link Random}.
     * @param chunkX         the chunk X coordinate of this chunk.
     * @param chunkZ         the chunk Z coordinate of this chunk.
     * @param world          : additionalData[0] The minecraft {@link World} we're generating for.
     * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
     * @param chunkProvider  : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
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
     * Called when the world is saved. When this happens finish generating all chunks
     *
     * @param event The event which will be ignored for all worlds but the overworld
     */
    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        // Overworld only
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
        {
            // Perform some finalization if we need to generate some chunks
            if (!this.chunksThatNeedGeneration.isEmpty())
            {
                event.getWorld().getMinecraftServer().sendMessage(new TextComponentTranslation("aotd.world_gen.saving", this.chunksThatNeedGeneration.size()));
                // Loop while we still need to generate a chunk
                while (!this.chunksThatNeedGeneration.isEmpty())
                {
                    // Grab the chunk to generate
                    ChunkPos chunkToGenerate = this.chunksThatNeedGeneration.remove();
                    // Generate the chunk
                    this.generate(event.getWorld(), chunkToGenerate);
                }
            }
        }
    }

    /**
     * Generates AOTD structures in a given world at the given position
     *
     * @param world    The world to generate in
     * @param chunkPos The position to generate at
     */
    private void generate(World world, ChunkPos chunkPos)
    {
        // Get the structure plan for the world
        IStructurePlan structurePlan = StructurePlan.get(world);
        // Make sure that our plan is valid
        if (structurePlan != null)
        {
            // Test if a structure exists at the X,Z coordinates
            if (structurePlan.structureExistsAt(chunkPos))
            {
                // The structure exists, grab it and the origin
                PlacedStructure placedStructure = structurePlan.getPlacedStructureAt(chunkPos);
                Structure structure = placedStructure.getStructure();
                NBTTagCompound data = placedStructure.getData();
                // Generate the structure
                structure.generate(world, chunkPos, data);
            }
        }
    }
}
