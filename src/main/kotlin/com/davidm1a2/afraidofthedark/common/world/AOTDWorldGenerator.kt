package com.davidm1a2.afraidofthedark.common.world

import com.davidm1a2.afraidofthedark.common.capabilities.world.StructurePlan
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.IWorldGenerator
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Class that takes care of all world generation from afraid of the dark
 *
 * @property chunksThatNeedGeneration A queue of chunk positions that need to be generated
 */
class AOTDWorldGenerator : IWorldGenerator {
    private val chunksThatNeedGeneration = ConcurrentLinkedQueue<ChunkPos>()

    /**
     * Adds a chunk position that needs regeneration
     *
     * @param chunkPos The position to regenerate
     */
    fun addChunkToRegenerate(chunkPos: ChunkPos) {
        chunksThatNeedGeneration.add(chunkPos)
    }

    /**
     * Called to generate a chunk of the world
     *
     * @param random         the chunk specific [Random].
     * @param chunkX         the chunk X coordinate of this chunk.
     * @param chunkZ         the chunk Z coordinate of this chunk.
     * @param world          : additionalData[0] The minecraft [World] we're generating for.
     * @param chunkGenerator : additionalData[1] The [IChunkProvider] that is generating.
     * @param chunkProvider  : additionalData[2] [IChunkProvider] that is requesting the world generation.
     */
    override fun generate(
        random: Random,
        chunkX: Int,
        chunkZ: Int,
        world: World,
        chunkGenerator: IChunkGenerator<*>,
        chunkProvider: IChunkProvider
    ) {
        // Create a chunk pos object for our chunk (X,Z) coords
        val chunkPos = ChunkPos(chunkX, chunkZ)
        // Generate the chunk
        this.generate(world, chunkPos)
    }

    /**
     * Tick handler tests if there is any structures awaiting generation and if so it consumes the structure and generates it. This is needed
     * because there's a chance that we place a structure in already generated chunks
     *
     * @param event The tick event
     */
    @SubscribeEvent
    fun onServerTick(event: ServerTickEvent) {
        // We can assume event.type == Type.SERVER and event.side == Side.SERVER because we are listening to the ServerTickEvent
        // Ensure phase is tick start to perform the generation
        /*
        if (event.phase == TickEvent.Phase.START) {
            // Test if we have a chunk to generate
            if (chunksThatNeedGeneration.isNotEmpty()) {
                // Grab the chunk to generate
                val chunkToGenerate = chunksThatNeedGeneration.remove()
                // Grab the world to generate which is the overworld (world 0)
                val world = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD)
                // Generate the chunk
                this.generate(world, chunkToGenerate)
            }
        }
         */
    }

    /**
     * Called when the world is saved. When this happens finish generating all chunks
     *
     * @param event The event which will be ignored for all worlds but the overworld
     */
    @SubscribeEvent
    fun onWorldSave(event: WorldEvent.Save) {
        // Overworld only
        /*
        if (!event.world.isRemote && event.world.dimension.type == DimensionType.OVERWORLD) {
            // Perform some finalization if we need to generate some chunks
            if (!chunksThatNeedGeneration.isEmpty()) {
                event.world.world.server!!.sendMessage(TextComponentTranslation(LocalizationConstants.WorldGen.SAVING, chunksThatNeedGeneration.size))

                // Loop while we still need to generate a chunk
                while (!chunksThatNeedGeneration.isEmpty()) {
                    // Grab the chunk to generate
                    val chunkToGenerate = chunksThatNeedGeneration.remove()
                    // Generate the chunk
                    this.generate(event.world.world, chunkToGenerate)
                }
            }
        }
         */
    }

    /**
     * Generates AOTD structures in a given world at the given position
     *
     * @param world    The world to generate in
     * @param chunkPos The position to generate at
     */
    private fun generate(world: World, chunkPos: ChunkPos) {
        // Get the structure plan for the world
        val structurePlan = StructurePlan.get(world)
        // Make sure that our plan is valid
        if (structurePlan != null) {
            // Test if a structure exists at the X,Z coordinates
            if (structurePlan.structureExistsAt(chunkPos)) {
                // The structure exists, grab it and the origin
                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
                val structure = placedStructure.structure
                val data = placedStructure.data

                // Generate the structure
                structure.generate(world, chunkPos, data)

                world.relightChunk(chunkPos)
            }
        }
    }
}