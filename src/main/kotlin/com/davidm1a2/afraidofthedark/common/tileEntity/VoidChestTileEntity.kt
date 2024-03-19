package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getVoidChestData
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.dimension.IslandUtility.getOrAssignPlayerPositionalIndex
import com.davidm1a2.afraidofthedark.common.dimension.teleport
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.network.packets.other.VoidChestPacket
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.BlockState
import net.minecraft.core.BlockPos
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.NameTagItem
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.LongNBT
import net.minecraft.tileentity.IChestLid
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.Constants
import java.util.UUID
import kotlin.math.sqrt

/**
 * Class that represents the void chest tile entity logic
 *
 * @constructor initializes the tile entity fields
 * @property lidAngle The chest's lid angle between 0 and 1 this tick
 * @property previousLidAngle The chest's lid angle last tick
 * @property shouldBeOpen True if the lid should be open, false otherwise. The chest will animate the lid according to this flag
 * @property owner The UUID of the player that owns this void chest
 * @property friends A list of friends that may visit this player's void chest
 * @property indexToGoTo The index of this owner's void chest position that this chest will send people to
 * @property playerToSend The current player that has opened the chest and will be sent soon
 * @property lastInteraction The last time (using system time) that the void chest was right clicked/interacted with
 */
@OnlyIn(value = Dist.CLIENT, _interface = IChestLid::class)
class VoidChestTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.VOID_CHEST), IChestLid {
    var lidAngle = 0f
        private set
    var previousLidAngle = 0f
        private set
    private var shouldBeOpen = false
    private var owner: UUID? = null
    private val friends: MutableSet<UUID> = mutableSetOf()
    private var indexToGoTo = 0
    private var playerToSend: PlayerEntity? = null
    private var lastInteraction: Long = 0

    override fun getOpenNess(partialTicks: Float): Float {
        return this.lidAngle
    }

    /**
     * Called every game tick to update the tile entity
     */
    override fun tick() {
        super.tick()
        val x = blockPos.x
        val y = blockPos.y
        val z = blockPos.z

        // Check every 20 ticks if it's time to close the chest or not
        if (ticksExisted % 10 == 0L) {
            if (System.currentTimeMillis() - lastInteraction > MILLIS_TO_CLOSE_CHEST) {
                shouldBeOpen = false
            }
        }

        // Set the last lid angle to be the current lid angle
        previousLidAngle = lidAngle

        // If the lid is closed and the chest should be open play the open sound
        if (shouldBeOpen && lidAngle == 0f) {
            level!!.playSound(
                null,
                x + 0.5,
                y + 0.5,
                z + 0.5,
                SoundEvents.CHEST_OPEN,
                SoundCategory.BLOCKS,
                0.5f,
                level!!.random.nextFloat() * 0.1f + 0.9f
            )
        }

        // If the chest should be open make it suck in the current player that opened the chest
        if (shouldBeOpen && playerToSend != null) {
            // Compute a vector pointing to the chest and then pull that player in that direction
            val velocity = Vector3d.atCenterOf(blockPos)
                .subtract(playerToSend!!.x, playerToSend!!.y, playerToSend!!.z)
                .normalize()
            val distanceSqToPlayer = playerToSend!!.distanceToSqr(Vector3d.atCenterOf(blockPos))

            // Inverse square law, force decreases as player gets more distant
            val adjustedVelocity = velocity.scale((PULL_FORCE * 1 / distanceSqToPlayer).coerceIn(0.01, 0.25))
            playerToSend!!.push(adjustedVelocity.x, adjustedVelocity.y, adjustedVelocity.z)
        }

        // Update the lid's angle if it's in transition from open to closed or closed to open
        if (!shouldBeOpen && lidAngle > 0 || shouldBeOpen && lidAngle < 1) {
            // Either add or subtract from the lid angle depending on if we're opening or closing
            lidAngle = if (shouldBeOpen) {
                lidAngle + OPEN_CLOSE_SPEED
            } else {
                lidAngle - OPEN_CLOSE_SPEED
            }

            // Clamp the angle between 0 and 1
            lidAngle = lidAngle.coerceIn(0f, 1f)

            // If the chest is closing play the closing sound and send the player to the void chest dimension
            if (lidAngle < 0.5 && previousLidAngle >= 0.5) {
                level!!.playSound(
                    null,
                    x + 0.5,
                    y + 0.5,
                    z + 0.5f.toDouble(),
                    SoundEvents.CHEST_CLOSE,
                    SoundCategory.BLOCKS,
                    0.5f,
                    level!!.random.nextFloat() * 0.1f + 0.9f
                )
                // Ensure the player we're sending is valid
                if (playerToSend != null) {
                    // Only allow void chest usage in the overworld
                    if (level!!.dimension() == World.OVERWORLD) {
                        // Temp, send the player to the void chest here if they are close enough to it
                        if (sqrt(playerToSend!!.distanceToSqr(Vector3d.atCenterOf(blockPos))) < DISTANCE_TO_SEND_PLAYER) {
                            if (!level!!.isClientSide) {
                                val playerVoidChestData = playerToSend!!.getVoidChestData()
                                // If the player we're sending is the owner send them to their home dimension, otherwise send them to their friend's dimension
                                if (playerToSend!!.gameProfile.id == owner) {
                                    playerVoidChestData.friendsIndex = -1
                                } else {
                                    playerVoidChestData.friendsIndex = indexToGoTo
                                }
                                (playerToSend as ServerPlayerEntity).teleport(ModDimensions.VOID_CHEST_WORLD)
                            }
                        }
                    } else {
                        if (!level!!.isClientSide) {
                            playerToSend!!.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.wrong_dimension"))
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when a player right clicks the chest
     *
     * @param entityPlayer The player that opened the chest
     */
    fun interact(entityPlayer: PlayerEntity) {
        // Server side processing only
        if (!level!!.isClientSide) {
            // If the chest has no owner attempt to set this player as the owner
            if (owner == null) {
                owner = entityPlayer.gameProfile.id
                val voidChestWorld: World = level!!.server!!.getLevel(ModDimensions.VOID_CHEST_WORLD)!!
                val playerVoidChestData = entityPlayer.getVoidChestData()
                indexToGoTo = getOrAssignPlayerPositionalIndex(voidChestWorld, playerVoidChestData)
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.owner_set", entityPlayer.gameProfile.name))
            } else if (entityPlayer.gameProfile.id == owner) {
                // Test if the player is holding a name tag. If so add/remove the friend, if not open the chest
                val heldItem = entityPlayer.mainHandItem
                if (heldItem.item is NameTagItem) {
                    // Grab the player's UUID
                    val friendsUUID = getID(heldItem.hoverName.string)

                    // If it's non-null continue, otherwise tell the player the name is wrong
                    if (friendsUUID != null) {
                        // If the chest does not have the friend add the friend
                        if (!friends.contains(friendsUUID)) {
                            friends.add(friendsUUID)
                            entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.friend_add", heldItem.hoverName.string))
                        } else {
                            friends.remove(friendsUUID)
                            entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.friend_remove", heldItem.hoverName.string))
                        }
                    } else {
                        entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.invalid_account", heldItem.hoverName.string))
                    }
                    MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entityPlayer, ModResearches.VOID_PARTY))
                } else {
                    openChest(entityPlayer)
                    AfraidOfTheDark.packetHandler.sendToDimension(VoidChestPacket(entityPlayer, blockPos.x, blockPos.y, blockPos.z), World.OVERWORLD)
                }
            } else if (friends.contains(entityPlayer.gameProfile.id)) {
                // Test if the player is trying to edit a chest's friend list that isnt theirs
                val heldItem = entityPlayer.mainHandItem
                if (heldItem.item is NameTagItem) {
                    entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.no_edit_access"))
                } else {
                    openChest(entityPlayer)
                    AfraidOfTheDark.packetHandler.sendToDimension(
                        VoidChestPacket(
                            entityPlayer,
                            blockPos.x,
                            blockPos.y,
                            blockPos.z
                        ), World.OVERWORLD
                    )
                }
            } else {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.no_access"))
            }
        }
    }

    /**
     * Called to open the chest with a given player in mind
     *
     * @param entityPlayer The player that opened the chest
     */
    fun openChest(entityPlayer: PlayerEntity?) {
        lastInteraction = System.currentTimeMillis()
        shouldBeOpen = true
        playerToSend = entityPlayer
    }

    /**
     * Gets the UUID for a given player username
     *
     * @param playerName The player's username
     * @return The player's UUID
     */
    private fun getID(playerName: String): UUID? {
        val gameProfileForUsername = level!!.server!!.profileCache.get(playerName)
        return gameProfileForUsername?.id
    }

    /**
     * Called to write this tile entities state to NBT data format
     *
     * @param compound The NBT compound to write to
     * @return The NBT compound representing this void chest tile entity
     */
    override fun save(compound: CompoundNBT): CompoundNBT {
        // Start by writing any default MC things to the NBT
        super.save(compound)

        // Write the owner as an ID
        if (owner != null) {
            compound.putUUID(NBT_OWNER, owner!!)
        }

        // Write the index to go to as an integer
        compound.putInt(NBT_INDEX_TO_GO_TO, indexToGoTo)

        val friendNBT = ListNBT()
        // For each friend append two new NBTLongs, one for most sig bits and one for least sig bits
        friends.forEach {
            friendNBT.add(LongNBT.valueOf(it.leastSignificantBits))
            friendNBT.add(LongNBT.valueOf(it.mostSignificantBits))
        }

        // Set the friends NBT to be this array
        compound.put(NBT_FRIENDS, friendNBT)
        return compound
    }

    /**
     * Reads this tile entity's state in from an NBT compound
     *
     * @param compound The NBT compound to read from
     */
    override fun load(blockState: BlockState, compound: CompoundNBT) {
        // Start by reading any default MC things from the NBT
        super.load(blockState, compound)

        // Read the owner tag, it could potentially not exist...
        owner = if (compound.contains(NBT_OWNER)) {
            compound.getUUID(NBT_OWNER)
        } else {
            null
        }

        // Read the index to go to tag
        indexToGoTo = compound.getInt(NBT_INDEX_TO_GO_TO)

        // Read all the chest's friends list in
        val friendIDParts = compound.getList(NBT_FRIENDS, Constants.NBT.TAG_LONG)
        var i = 0
        while (i < friendIDParts.size) {
            val friendIDLeast = friendIDParts[i]
            val friendIDMost = friendIDParts[i + 1]
            // Ensure the tags are the correct type
            if (friendIDLeast is LongNBT && friendIDMost is LongNBT) {
                // Add the friend back in from least and most significant bits
                friends.add(UUID(friendIDMost.asLong, friendIDLeast.asLong))
            }
            i = i + 2
        }
    }

    companion object {
        // NBT tag constants
        private const val NBT_OWNER = "owner"
        private const val NBT_INDEX_TO_GO_TO = "index_to_go_to"
        private const val NBT_FRIENDS = "friends"

        // Chest constants
        private const val MILLIS_TO_CLOSE_CHEST = 2000
        private const val PULL_FORCE = 1.0
        private const val OPEN_CLOSE_SPEED = 0.1f
        private const val DISTANCE_TO_SEND_PLAYER = 2.0
    }
}