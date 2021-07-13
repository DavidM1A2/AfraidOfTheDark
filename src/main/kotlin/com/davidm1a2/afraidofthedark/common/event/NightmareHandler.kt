package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.client.sound.EerieEchoSound
import com.davidm1a2.afraidofthedark.client.sound.NightmareChaseMusicSound
import com.davidm1a2.afraidofthedark.client.sound.NightmareMusicSound
import com.davidm1a2.afraidofthedark.common.capabilities.getNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
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
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ChestScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import java.util.*

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
            if (world.dimension.type == ModDimensions.NIGHTMARE_TYPE) {
                if (world.gameTime % 20 == 0L) {
                    (world as ServerWorld).entities
                        .filter { it?.type == ModEntities.GHASTLY_ENARIA }
                        .map { it as GhastlyEnariaEntity }
                        .filter { it.getTouchedPlayer() != null }
                        .forEach {
                            val player = world.getEntityByUuid(it.getTouchedPlayer()!!)
                            if (player != null && player is ServerPlayerEntity) {
                                // Kill enaria, she's now unloaded (can't use .setDead()) or we get an index out of bounds exception?
                                it.onKillCommand()

                                // Dismount whatever we're in
                                player.stopRiding()

                                // Give the player a nightmare stone
                                player.inventory.addItemStackToInventory(ItemStack(ModItems.NIGHTMARE_STONE))

                                // Send them back to their original dimension
                                player.teleport(player.getNightmareData().preTeleportDimension!!)
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
        if (!entityPlayer.world.isRemote) {
            // If the player has a sleeping potion effect on and has the right researches send them to the nightmare
            if (entityPlayer.getActivePotionEffect(ModEffects.SLEEPING) != null) {
                val playerResearch = entityPlayer.getResearch()

                // If the player can research the nightmare research do so
                if (playerResearch.canResearch(ModResearches.NIGHTMARE)) {
                    playerResearch.setResearch(ModResearches.NIGHTMARE, true)
                    playerResearch.sync(entityPlayer, true)
                }

                // If the player has the nightmare research send them to the nightmare realm
                if (playerResearch.isResearched(ModResearches.NIGHTMARE)) {
                    event.setResult(PlayerEntity.SleepResult.OTHER_PROBLEM)
                    (entityPlayer as ServerPlayerEntity).teleport(ModDimensions.NIGHTMARE_TYPE)
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
        if (!event.world.isRemote) {
            // Test if we're in the nightmare dimension
            if (event.world.dimension.type == ModDimensions.NIGHTMARE_TYPE) {
                // Search through the list of entities in the chunk
                val chunk = event.chunk
                if (chunk is Chunk) {
                    for (entityMap in chunk.entityLists) {
                        // Go through each entity
                        for (entity in entityMap) {
                            // If an entity is enaria, kill her and respawn her closer to the player if possible
                            if (entity is GhastlyEnariaEntity) {
                                // If the enaria entity is dead the player touched her and went back to the overworld
                                if (entity.isAlive) {
                                    // Kill any unloaded enaria entities
                                    entity.remove()

                                    // Grab the nearby player
                                    val entityPlayer = event.world.getClosestPlayer(
                                        entity,
                                        Constants.DISTANCE_BETWEEN_ISLANDS / 2.toDouble()
                                    )

                                    // If we have a valid nearby player teleport a new enaria to them. If we don't then just die (the player left the nightmare dimension)
                                    if (entityPlayer != null && entityPlayer.isAlive) {
                                        // Compute a random offset in +/- 25-50 in x and z
                                        val random = entityPlayer.rng
                                        val offsetX =
                                            if (random.nextBoolean()) random.nextInt(26) - 50 else random.nextInt(26) + 25
                                        val offsetZ =
                                            if (random.nextBoolean()) random.nextInt(26) - 50 else random.nextInt(26) + 25

                                        // Compute enaria's new position
                                        val posX = entityPlayer.position.x + offsetX
                                        val posZ = entityPlayer.position.z + offsetZ

                                        // Spawn a new enaria
                                        val newEnaria = GhastlyEnariaEntity(ModEntities.GHASTLY_ENARIA, event.world.world)
                                        newEnaria.setBenign(!entityPlayer.getResearch().isResearched(ModResearches.ENARIA))
                                        newEnaria.setPosition(posX.toDouble(), entityPlayer.posY, posZ.toDouble())
                                        event.world.addEntity(newEnaria)

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
        if (!event.player.world.isRemote) {
            if (event.player.dimension == ModDimensions.NIGHTMARE_TYPE) {
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
    @OnlyIn(Dist.CLIENT)
    fun onEntityJoinWorldEvent(event: EntityJoinWorldEvent) {
        // Client side only, even though this must be true since we're using SideOnly
        if (event.world.isRemote) {
            val entity = event.entity

            // Test if the player is going to the nightmare
            if (event.world.dimension.type.modType == ModDimensions.NIGHTMARE && entity == Minecraft.getInstance().player) {
                // We need one more check to see if the player's dimension id is nightmare. This is a workaround because
                // when teleporting this callback will get fired twice since the player teleports once for
                // the teleport, once to be spawned into the world
                if (entity.world.dimension.type.modType == ModDimensions.NIGHTMARE) {
                    // Grab the client's sound handler and play the sound if it is not already playing
                    val soundHandler = Minecraft.getInstance().soundHandler

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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun onGuiOpen(event: GuiOpenEvent) {
        // Can't open an echest in the nightmare realm
        val player = Minecraft.getInstance().player
        if (player?.dimension?.modType == ModDimensions.NIGHTMARE) {
            val gui = event.gui
            if (gui is ChestScreen) {
                val title = gui.title
                if (title is TranslationTextComponent && title.key.contains("enderchest")) {
                    // Close the chest and don't show the gui
                    gui.container.onContainerClosed(player)
                    event.isCanceled = true
                    player.sendMessage(TranslationTextComponent("message.afraidofthedark.nightmare.enderchest"))
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
        if (!event.entity.world.isRemote) {
            // Get to and from dimension
            val fromDimension = event.entity.dimension
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
    private fun processPreTeleport(entityPlayer: ServerPlayerEntity, dimensionFrom: DimensionType, dimensionTo: DimensionType): Boolean {
        // If we're going to dimension NIGHTMARE then we need to do some preprocesing and tests to ensure the player can continue
        if (dimensionTo == ModDimensions.NIGHTMARE_TYPE) {
            // We can't go from nightmare to nightmare
            if (dimensionFrom == ModDimensions.NIGHTMARE_TYPE) {
                return true
            }

            // Any other dimension is valid. We can go from any dimension other than the nightmare to the nightmare
            // We need to store off player position data pre-teleport
            val playerNightmareData = entityPlayer.getNightmareData()

            // Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
            // new portal block. This ensure you don't get stuck in a teleport loop
            // First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
            if (IslandUtility.isValidSpawnLocation(entityPlayer.world, entityPlayer.position)) {
                playerNightmareData.preTeleportPosition = entityPlayer.position
            } else {
                val preTeleportPosition = IslandUtility.findValidSpawnLocation(
                    entityPlayer.world,
                    entityPlayer.position,
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
            val inventoryNBT = entityPlayer.inventory.write(ListNBT())
            playerNightmareData.preTeleportPlayerInventory = inventoryNBT

            // Clear the players inventory and sync it with packets
            entityPlayer.inventory.clear()
            entityPlayer.container.detectAndSendChanges()
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
        if (!event.player.world.isRemote) {
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
    private fun processPostTeleport(entityPlayer: ServerPlayerEntity, dimensionFrom: DimensionType, dimensionTo: DimensionType) {
        // If the player entered the nightmare dimension then set their position
        val playerNightmareData = entityPlayer.getNightmareData()

        if (dimensionTo == ModDimensions.NIGHTMARE_TYPE) {
            val nightmareWorld = entityPlayer.server!!.getWorld(ModDimensions.NIGHTMARE_TYPE)
            // Compute the player's index to go to
            val indexToGoTo = IslandUtility.getOrAssignPlayerPositionalIndex(nightmareWorld, playerNightmareData)
            // Compute the player's X position based on the index
            val playerXBase = indexToGoTo * Constants.DISTANCE_BETWEEN_ISLANDS

            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.setPlayerLocation(playerXBase + 21.5, 74.0, 44.5, 0f, 0f)

            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer)

            // Give the player a research journal
            entityPlayer.inventory.addItemStackToInventory(createNamedJournal(entityPlayer))
            // Give the player a hint book to find the researches
            entityPlayer.inventory.addItemStackToInventory(ItemStack(ModItems.INSANITYS_HEIGHTS))
            // Give the player torches to see
            entityPlayer.inventory.addItemStackToInventory(ItemStack(Blocks.TORCH, 64))

            // Test if the player needs their spell creation structure generated as an addon to the nightmare island
            testForEnariasAltar(entityPlayer, nightmareWorld, BlockPos(playerXBase, 0, 0))
        }

        // If the player left the nightmare reset their position
        if (dimensionFrom == ModDimensions.NIGHTMARE_TYPE) {
            // Grab the player's pre-teleport position
            val preTeleportPosition = playerNightmareData.preTeleportPosition!!

            // Reset the player's position
            entityPlayer.connection.setPlayerLocation(
                preTeleportPosition.x + 0.5,
                preTeleportPosition.y + 1.5,
                preTeleportPosition.z + 0.5,
                0f,
                0f
            )

            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer)

            // Figure out how many nightmare stones the player has
            val numberNightmareStones = entityPlayer.inventory
                .clearMatchingItems({ it.item == ModItems.NIGHTMARE_STONE }, -1)
                .coerceAtMost(64)

            // Clear the nightmare junk out of the player's inventory
            entityPlayer.inventory.clear()

            // Update the player's inventory with the original things
            entityPlayer.inventory.read(playerNightmareData.preTeleportPlayerInventory!!)

            // If the player found a nightmare stone save it
            if (numberNightmareStones > 0) {
                entityPlayer.inventory.addItemStackToInventory(ItemStack(ModItems.NIGHTMARE_STONE, numberNightmareStones))
            }

            entityPlayer.container.detectAndSendChanges()
        }
    }

    /**
     * Called to reset a player's stats after teleportation
     *
     * @param entityPlayer The player to reset stats for
     */
    private fun resetPlayerStats(entityPlayer: PlayerEntity) {
        entityPlayer.motion = Vec3d(0.0, 0.0, 0.0)
        entityPlayer.health = 20f
        entityPlayer.foodStats.foodLevel = 20
        // Clear active potion effects
        entityPlayer.clearActivePotions()
    }

    /**
     * Creates a blood stained journal that is named after the player
     *
     * @param entityPlayer The player to create a journal for
     * @return The created journal
     */
    private fun createNamedJournal(entityPlayer: PlayerEntity): ItemStack {
        val toReturn = ItemStack(ModItems.JOURNAL, 1)
        ModItems.JOURNAL.setOwner(toReturn, entityPlayer.gameProfile.name)
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
        if (playerResearch.isResearched(ModResearches.ENARIA)) {
            // If enaria's alter does not exist generate the schematic
            if (nightmareWorld.getBlockState(islandPos.add(101, 74, 233)).block !== ModBlocks.ENARIAS_ALTAR) {
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

                enariasAltar.create(
                    nightmareWorld,
                    nightmareWorld.chunkProvider.chunkGenerator,
                    // Random isn't used
                    throwawayRandom,
                    MutableBoundingBox(
                        posX,
                        posZ,
                        posX + ModSchematics.ENARIAS_ALTAR.getWidth(),
                        posZ + ModSchematics.ENARIAS_ALTAR.getLength()
                    ),
                    // ChunkPos is ignored
                    ChunkPos(0, 0)
                )
            }
        }
    }

    companion object {
        // Constant number of blocks to search for a spawn position
        private const val VALID_SPAWN_SEARCH_DISTANCE = 6
    }
}