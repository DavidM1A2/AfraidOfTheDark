package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.client.sound.EerieEchoSound
import com.davidm1a2.afraidofthedark.client.sound.NightmareChaseMusicSound
import com.davidm1a2.afraidofthedark.client.sound.NightmareMusicSound
import com.davidm1a2.afraidofthedark.common.capabilities.getNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.RespawnPosition
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModEffects
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.dimension.IslandUtility
import com.davidm1a2.afraidofthedark.common.dimension.teleport
import com.davidm1a2.afraidofthedark.common.entity.enaria.GhastlyEnariaEntity
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraft.util.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import java.util.Random

/**
 * Class handling events to send players to and from their nightmare realm
 */
class NightmareHandler {
    /**
     * Called when the nightmare world ticks, go over Enaria entities that have hit a player and send that player back to the overworld
     */
    @SubscribeEvent
    fun onNightmareWorldTick(event: TickEvent.WorldTickEvent) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val world = event.world
            if (world.dimension() == ModDimensions.NIGHTMARE_WORLD) {
                if (world.gameTime % 20 == 0L) {
                    (world as ServerWorld).entities
                        .filter { it?.type == ModEntities.GHASTLY_ENARIA }
                        .map { it as GhastlyEnariaEntity }
                        .filter { it.getTouchedPlayer() != null }
                        .forEach {
                            val player = world.getEntity(it.getTouchedPlayer()!!)
                            if (player != null && player is ServerPlayerEntity) {
                                // Kill enaria, she's now unloaded (can't use .setDead()) or we get an index out of bounds exception?
                                it.kill()

                                // Dismount whatever we're in
                                player.stopRiding()

                                // Give the player a nightmare stone
                                player.inventory.add(ItemStack(ModItems.NIGHTMARE_STONE))

                                // Send them back to their original dimension
                                player.teleport(player.getNightmareData().preTeleportDimension!!)

                                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(player, ModResearches.ENARIA))
                            }
                            it.clearTouchedPlayer()
                        }
                }
            }
        }
    }

    /**
     * Called when the player sleeps in a bed, tests if they're drowsy and if so sends them to the nightmare realm
     *
     * @param event event containing player and world data
     */
    @SubscribeEvent
    fun onPlayerSleepInBedEvent(event: PlayerSleepInBedEvent) {
        val entityPlayer = event.player
        // Only process server side
        if (!entityPlayer.level.isClientSide) {
            // If the player has a sleeping potion effect on and has the right researches send them to the nightmare
            if (entityPlayer.activeEffectsMap[ModEffects.SLEEPING] != null) {
                val playerResearch = entityPlayer.getResearch()

                // If the player has the nightmare research (or can research it) send them to the nightmare realm
                if (playerResearch.isResearched(ModResearches.NIGHTMARE_REALM.preRequisite!!)) {
                    event.setResult(PlayerEntity.SleepResult.OTHER_PROBLEM)
                    (entityPlayer as ServerPlayerEntity).teleport(ModDimensions.NIGHTMARE_WORLD)
                }
            }
        }
    }

    /**
     * Called when a chunk is unloaded from the world
     *
     * @param event The event containing info about the unload
     */
    @SubscribeEvent
    fun onChunkUnloadEvent(event: ChunkEvent.Unload) {
        // Server side processing only
        val world = event.world
        if (!world.isClientSide) {
            if (world !is World) {
                throw IllegalStateException("Chunk unload event called with an IWorld that was not a world")
            }

            // Test if we're in the nightmare dimension
            if (world.dimension() == ModDimensions.NIGHTMARE_WORLD) {
                // Search through the list of entities in the chunk
                val chunk = event.chunk
                if (chunk is Chunk) {
                    for (entityMap in chunk.entitySections) {
                        // Go through each entity
                        for (entity in entityMap) {
                            // If an entity is enaria, kill her and respawn her closer to the player if possible
                            if (entity is GhastlyEnariaEntity) {
                                // If the enaria entity is dead the player touched her and went back to the overworld
                                if (entity.isAlive) {
                                    // Kill any unloaded enaria entities
                                    entity.remove()

                                    // Grab the nearby player
                                    val entityPlayer = world.getNearestPlayer(
                                        entity,
                                        Constants.DISTANCE_BETWEEN_ISLANDS / 2.toDouble()
                                    )

                                    // If we have a valid nearby player teleport a new enaria to them. If we don't then just die (the player left the nightmare dimension)
                                    if (entityPlayer != null && entityPlayer.isAlive) {
                                        // Compute a random offset in +/- 25-50 in x and z
                                        val random = entityPlayer.random
                                        val offsetX =
                                            if (random.nextBoolean()) random.nextInt(26) - 50 else random.nextInt(26) + 25
                                        val offsetZ =
                                            if (random.nextBoolean()) random.nextInt(26) - 50 else random.nextInt(26) + 25

                                        // Compute enaria's new position
                                        val posX = entityPlayer.x + offsetX
                                        val posZ = entityPlayer.z + offsetZ

                                        // Spawn a new enaria
                                        val newEnaria = GhastlyEnariaEntity(ModEntities.GHASTLY_ENARIA, world)
                                        newEnaria.setBenign(!entityPlayer.getResearch().isResearched(ModResearches.ARCH_SORCERESS))
                                        newEnaria.setPos(posX, entityPlayer.y, posZ)
                                        world.addFreshEntity(newEnaria)

                                        // Return out of here, there will only be 1 enaria following the player
                                        return
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the player dies, teleport them back to the dimension they came from
     *
     * @param event The data of the respawned player
     */
    @SubscribeEvent
    fun onPlayerRespawnEvent(event: PlayerEvent.PlayerRespawnEvent) {
        // Server side processing only
        if (!event.player.level.isClientSide) {
            if (event.player.level.dimension() == ModDimensions.NIGHTMARE_WORLD) {
                val nightmareData = event.player.getNightmareData()
                // Send the player back to their original dimension
                (event.player as ServerPlayerEntity).teleport(nightmareData.preTeleportDimension!!)
            }
        }
    }

    /**
     * Called when an entity joins a world. Check if a player entered the nightmare and if so play the nightmare
     * bells and echo. Only do this client side since ISound is a client side only class
     *
     * @param event Event containing info about who joined the world
     */
    @SubscribeEvent
    fun onEntityJoinWorldEvent(event: EntityJoinWorldEvent) {
        // Client side only, even though this must be true since we're using SideOnly
        if (event.world.isClientSide) {
            val entity = event.entity

            // Test if the player is going to the nightmare
            if (event.world.dimension() == ModDimensions.NIGHTMARE_WORLD && entity == Minecraft.getInstance().player) {
                // We need one more check to see if the player's dimension id is nightmare. This is a workaround because
                // when teleporting this callback will get fired twice since the player teleports once for
                // the teleport, once to be spawned into the world
                if (entity.level.dimension() == ModDimensions.NIGHTMARE_WORLD) {
                    // Grab the client's sound handler and play the sound if it is not already playing
                    val soundHandler = Minecraft.getInstance().soundManager

                    // Play the eerie echo sound after 3 seconds followed by the enaria music after 7
                    soundHandler.playDelayed(EerieEchoSound(), 3 * 20)
                    // Play both music types, one will automatically disable itself based on player research. We can't
                    // test player research here because it isn't synced from Server -> Client at this point
                    soundHandler.playDelayed(NightmareMusicSound(), 7 * 20)
                    soundHandler.playDelayed(NightmareChaseMusicSound(), 7 * 20)
                }
            }
        }
    }

    /**
     * When we want to travel to the
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    fun onPreEntityTravelToDimension(event: EntityTravelToDimensionEvent) {
        // Server side processing only
        if (!event.entity.level.isClientSide) {
            // Get to and from dimension
            val fromDimension = event.entity.level.dimension()
            val toDimension = event.dimension

            // Test if the entity is a player, if so process it
            if (event.entity is ServerPlayerEntity) {
                val entityPlayer = event.entity as ServerPlayerEntity

                // Process the pre-teleport server side, if it returns true then we cancel the TP
                if (processPreTeleport(entityPlayer, fromDimension, toDimension)) {
                    event.isCanceled = true
                }
            }
        }
    }

    /**
     * Called right before the player is about to teleport
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player is currently in
     * @param dimensionTo   The dimension the player is going to
     * @return True to cancel the teleport, false otherwise
     */
    private fun processPreTeleport(entityPlayer: ServerPlayerEntity, dimensionFrom: RegistryKey<World>, dimensionTo: RegistryKey<World>): Boolean {
        // If we're going to dimension NIGHTMARE then we need to do some preprocesing and tests to ensure the player can continue
        if (dimensionTo == ModDimensions.NIGHTMARE_WORLD) {
            // We can't go from nightmare to nightmare
            if (dimensionFrom == ModDimensions.NIGHTMARE_WORLD) {
                return true
            }

            // Any other dimension is valid. We can go from any dimension other than the nightmare to the nightmare
            // We need to store off player position data pre-teleport
            val playerNightmareData = entityPlayer.getNightmareData()

            // Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
            // new portal block. This ensure you don't get stuck in a teleport loop
            // First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
            if (IslandUtility.isValidSpawnLocation(entityPlayer.level, entityPlayer.blockPosition())) {
                playerNightmareData.preTeleportPosition = entityPlayer.blockPosition()
            } else {
                val preTeleportPosition = IslandUtility.findValidSpawnLocation(
                    entityPlayer.level,
                    entityPlayer.blockPosition(),
                    VALID_SPAWN_SEARCH_DISTANCE
                )

                // If we didn't find a valid spot around the player's position then throw an error and reject the teleport
                if (preTeleportPosition == null) {
                    entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.dimension.nightmare.no_spawn"))
                    return true
                } else {
                    playerNightmareData.preTeleportPosition = preTeleportPosition
                }
            }

            // Set our pre-teleport dimension ID
            playerNightmareData.preTeleportDimension = dimensionFrom

            // Write our player's inventory to NBT and save it off
            val inventoryNBT = entityPlayer.inventory.save(ListNBT())
            playerNightmareData.preTeleportPlayerInventory = inventoryNBT

            // Write our player's respawn position to NBT
            playerNightmareData.preTeleportRespawnPosition = RespawnPosition(
                entityPlayer.respawnPosition,
                entityPlayer.respawnDimension,
                entityPlayer.respawnAngle,
                entityPlayer.isRespawnForced
            )

            // Clear the players inventory and sync it with packets
            entityPlayer.inventory.clearContent()
            entityPlayer.inventoryMenu.broadcastChanges()
        }
        return false
    }

    /**
     * Called when any player is finished changing dimensions
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    fun onPostEntityTravelToDimension(event: PlayerEvent.PlayerChangedDimensionEvent) {
        // Perform all the important logic server side
        if (!event.player.level.isClientSide) {
            // Get to and from dimension
            val fromDimension = event.from
            val toDimension = event.to

            // Get the player teleporting
            val entityPlayer = event.player as ServerPlayerEntity

            // Process the post-teleport server side
            processPostTeleport(entityPlayer, fromDimension, toDimension)
        }
    }

    /**
     * Called right after the player teleported
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player was in
     * @param dimensionTo   The dimension the player is now in
     */
    private fun processPostTeleport(entityPlayer: ServerPlayerEntity, dimensionFrom: RegistryKey<World>, dimensionTo: RegistryKey<World>) {
        // If the player entered the nightmare dimension then set their position
        val playerNightmareData = entityPlayer.getNightmareData()

        if (dimensionTo == ModDimensions.NIGHTMARE_WORLD) {
            val nightmareWorld = entityPlayer.server!!.getLevel(ModDimensions.NIGHTMARE_WORLD)
            // Compute the player's index to go to
            val indexToGoTo = IslandUtility.getOrAssignPlayerPositionalIndex(nightmareWorld!!, playerNightmareData)
            // Compute the player's X position based on the index
            val playerXBase = indexToGoTo * Constants.DISTANCE_BETWEEN_ISLANDS

            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.teleport(playerXBase + 21.5, 74.0, 44.5, 0f, 0f)
            entityPlayer.fallDistance = 0f

            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer)

            // Give the player a research journal
            entityPlayer.inventory.add(createNamedJournal(entityPlayer))
            // Give the player a hint book to find the researches
            entityPlayer.inventory.add(ItemStack(ModItems.INSANITYS_HEIGHTS))
            // Give the player torches to see
            entityPlayer.inventory.add(ItemStack(Blocks.TORCH, 64))

            // Ensure the player respawns here before teleporting back
            entityPlayer.setRespawnPosition(
                ModDimensions.NIGHTMARE_WORLD,
                // Respawn the player in the sky, so they don't accidentally spawn within Enaria
                entityPlayer.blockPosition().above(1024),
                0f,
                true,
                false
            )

            // Test if the player needs their spell creation structure generated as an addon to the nightmare island
            testForEnariasAltar(entityPlayer, nightmareWorld, BlockPos(playerXBase, 0, 0))
        }

        // If the player left the nightmare reset their position
        if (dimensionFrom == ModDimensions.NIGHTMARE_WORLD) {
            // Grab the player's pre-teleport position
            val preTeleportPosition = playerNightmareData.preTeleportPosition!!

            // Reset the player's position
            entityPlayer.connection.teleport(
                preTeleportPosition.x + 0.5,
                preTeleportPosition.y + 1.5,
                preTeleportPosition.z + 0.5,
                0f,
                0f
            )
            entityPlayer.fallDistance = 0f

            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer)

            // Figure out how many nightmare stones the player has
            val numberNightmareStones = ItemStackHelper.clearOrCountMatchingItems(entityPlayer.inventory, { it.item == ModItems.NIGHTMARE_STONE }, -1, true)
                .coerceAtMost(64)

            // Clear the nightmare junk out of the player's inventory
            entityPlayer.inventory.clearContent()

            // Restore the player's inventory
            entityPlayer.inventory.load(playerNightmareData.preTeleportPlayerInventory!!)

            // Restore the player's respawn point
            val respawnPosition = playerNightmareData.preTeleportRespawnPosition!!
            entityPlayer.setRespawnPosition(
                respawnPosition.respawnDimension,
                respawnPosition.respawnPosition,
                respawnPosition.respawnAngle,
                respawnPosition.respawnForced,
                false
            )

            // If the player found a nightmare stone save it
            if (numberNightmareStones > 0) {
                entityPlayer.inventory.add(ItemStack(ModItems.NIGHTMARE_STONE, numberNightmareStones))
            }

            entityPlayer.inventoryMenu.broadcastChanges()

            MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entityPlayer, ModResearches.SLEEPING_POTION))
        }
    }

    /**
     * Called to reset a player's stats after teleportation
     *
     * @param entityPlayer The player to reset stats for
     */
    private fun resetPlayerStats(entityPlayer: PlayerEntity) {
        entityPlayer.deltaMovement = Vector3d(0.0, 0.0, 0.0)
        entityPlayer.health = 20f
        entityPlayer.foodData.foodLevel = 20
        // Clear active potion effects
        entityPlayer.activeEffects.map { it.effect }.forEach {
            entityPlayer.removeEffect(it)
        }
    }

    /**
     * Creates a journal that is named after the player
     *
     * @param entityPlayer The player to create a journal for
     * @return The created journal
     */
    private fun createNamedJournal(entityPlayer: PlayerEntity): ItemStack {
        val toReturn = ItemStack(ModItems.ARCANE_JOURNAL, 1)
        ModItems.ARCANE_JOURNAL.setOwner(toReturn, entityPlayer.gameProfile.name)
        return toReturn
    }

    /**
     * Tests if we need to generate enaria's altar. Do this if the player has beaten enaria and the altar doesn't
     * exist yet
     *
     * @param entityPlayer   The player that killed enaria and is being checked for
     * @param nightmareWorld The nightmare world being tested
     * @param islandPos      The position of this player's island realm
     */
    private fun testForEnariasAltar(entityPlayer: ServerPlayerEntity, nightmareWorld: ServerWorld, islandPos: BlockPos) {
        // Grab the player's research, if he has enaria generate the altar if needed
        val playerResearch = entityPlayer.getResearch()
        if (playerResearch.isResearched(ModResearches.ARCH_SORCERESS)) {
            // If enaria's alter does not exist generate the schematic
            if (nightmareWorld.getBlockState(islandPos.offset(101, 74, 233)).block !== ModBlocks.ENARIAS_ALTAR) {
                val posX = islandPos.x + 67
                val posY = islandPos.y + 40
                val posZ = islandPos.z + 179
                val throwawayRandom = Random()
                val enariasAltar = SchematicStructurePiece(
                    posX,
                    posY,
                    posZ,
                    throwawayRandom,
                    ModSchematics.ENARIAS_ALTAR,
                    null,
                    Direction.NORTH
                )

                enariasAltar.postProcess(
                    nightmareWorld,
                    nightmareWorld.structureFeatureManager(),
                    nightmareWorld.chunkSource.generator,
                    // Random isn't used
                    throwawayRandom,
                    MutableBoundingBox(
                        posX,
                        posZ,
                        posX + ModSchematics.ENARIAS_ALTAR.getWidth(),
                        posZ + ModSchematics.ENARIAS_ALTAR.getLength()
                    ),
                    // ChunkPos is ignored
                    ChunkPos(0, 0),
                    BlockPos.ZERO
                )
            }
        }
    }

    companion object {
        // Constant number of blocks to search for a spawn position
        private const val VALID_SPAWN_SEARCH_DISTANCE = 6
    }
}