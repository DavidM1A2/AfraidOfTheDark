package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getVoidChestData
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.dimension.IslandUtility.getOrAssignPlayerPositionalIndex
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.VoidChestPacket
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemNameTag
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagLong
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.util.Constants
import java.util.*
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
class TileEntityVoidChest : AOTDTickingTileEntity(ModTileEntities.VOID_CHEST) {
    var lidAngle = 0f
        private set
    var previousLidAngle = 0f
        private set
    private var shouldBeOpen = false
    private var owner: UUID? = null
    private val friends: MutableSet<UUID> = mutableSetOf()
    private var indexToGoTo = 0
    private var playerToSend: EntityPlayer? = null
    private var lastInteraction: Long = 0

    /**
     * Called every game tick to update the tile entity
     */
    override fun tick() {
        super.tick()
        val x = pos.x
        val y = pos.y
        val z = pos.z

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
            world.playSound(
                x + 0.5,
                y + 0.5,
                z + 0.5,
                SoundEvents.BLOCK_CHEST_OPEN,
                SoundCategory.BLOCKS,
                0.5f,
                world.rand.nextFloat() * 0.1f + 0.9f,
                false
            )
        }

        // If the chest should be open make it suck in the current player that opened the chest
        if (shouldBeOpen && playerToSend != null) {
            // Compute a vector pointing to the chest and then pull that player in that direction
            val velocity = Vec3d(pos)
                .add(0.5, 0.5, 0.5)
                .subtract(playerToSend!!.posX, playerToSend!!.posY, playerToSend!!.posZ)
                .normalize()
            val distanceSqToPlayer = playerToSend!!.getDistanceSq(pos)

            // Inverse square law, force decreases as player gets more distant
            val adjustedVelocity = velocity.scale((PULL_FORCE * 1 / distanceSqToPlayer).coerceIn(0.01, 0.25))
            playerToSend!!.addVelocity(adjustedVelocity.x, adjustedVelocity.y, adjustedVelocity.z)
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
                world.playSound(
                    x + 0.5,
                    y + 0.5,
                    z + 0.5f.toDouble(),
                    SoundEvents.BLOCK_CHEST_CLOSE,
                    SoundCategory.BLOCKS,
                    0.5f,
                    world.rand.nextFloat() * 0.1f + 0.9f,
                    false
                )
                // Ensure the player we're sending is valid
                if (playerToSend != null) {
                    val currentDimensionID = world.dimension.type
                    // Only allow void chest usage in the overworld
                    if (currentDimensionID == DimensionType.OVERWORLD) {
                        // Temp, send the player to the void chest here if they are close enough to it
                        if (sqrt(playerToSend!!.getDistanceSq(getPos())) < DISTANCE_TO_SEND_PLAYER) {
                            if (!world.isRemote) {
                                val playerVoidChestData = playerToSend!!.getVoidChestData()
                                // If the player we're sending is the owner send them to their home dimension, otherwise send them to their friend's dimension
                                if (playerToSend!!.gameProfile.id == owner) {
                                    playerVoidChestData.friendsIndex = -1
                                } else {
                                    playerVoidChestData.friendsIndex = indexToGoTo
                                }
                                playerToSend!!.changeDimension(
                                    ModDimensions.VOID_CHEST_TYPE,
                                    ModDimensions.NOOP_TELEPORTER
                                )
                            }
                        }
                    } else {
                        if (!world.isRemote) {
                            playerToSend!!.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.WRONG_DIMENSION))
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
    fun interact(entityPlayer: EntityPlayer) {
        // Server side processing only
        if (!world.isRemote) {
            // If the chest has no owner attempt to set this player as the owner
            if (owner == null) {
                owner = entityPlayer.gameProfile.id
                val voidChestWorld: World = world.server!!.getWorld(ModDimensions.VOID_CHEST_TYPE)
                val playerVoidChestData = entityPlayer.getVoidChestData()
                indexToGoTo = getOrAssignPlayerPositionalIndex(voidChestWorld, playerVoidChestData)
                entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.OWNER_SET, entityPlayer.gameProfile.name))
            } else if (entityPlayer.gameProfile.id == owner) {
                // Test if the player is holding a name tag. If so add/remove the friend, if not open the chest
                val heldItem = entityPlayer.heldItemMainhand
                if (heldItem.item is ItemNameTag) {
                    // Grab the player's UUID
                    val friendsUUID = getID(heldItem.displayName.formattedText)

                    // If it's non-null continue, otherwise tell the player the name is wrong
                    if (friendsUUID != null) {
                        // If the chest does not have the friend add the friend
                        if (!friends.contains(friendsUUID)) {
                            friends.add(friendsUUID)
                            entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.FRIEND_ADD, heldItem.displayName))
                        } else {
                            friends.remove(friendsUUID)
                            entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.FRIEND_REMOVE, heldItem.displayName))
                        }
                    } else {
                        entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.INVALID_ACCOUNT, heldItem.displayName))
                    }
                } else {
                    openChest(entityPlayer)
                    AfraidOfTheDark.packetHandler.sendToDimension(
                        VoidChestPacket(
                            entityPlayer,
                            pos.x,
                            pos.y,
                            pos.z
                        ), DimensionType.OVERWORLD
                    )
                }
            } else if (friends.contains(entityPlayer.gameProfile.id)) {
                // Test if the player is trying to edit a chest's friend list that isnt theirs
                val heldItem = entityPlayer.heldItemMainhand
                if (heldItem.item is ItemNameTag) {
                    entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.NO_EDIT_ACCESS))
                } else {
                    openChest(entityPlayer)
                    AfraidOfTheDark.packetHandler.sendToDimension(
                        VoidChestPacket(
                            entityPlayer,
                            pos.x,
                            pos.y,
                            pos.z
                        ), DimensionType.OVERWORLD
                    )
                }
            } else {
                entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.NO_ACCESS))
            }
        }
    }

    /**
     * Called to open the chest with a given player in mind
     *
     * @param entityPlayer The player that opened the chest
     */
    fun openChest(entityPlayer: EntityPlayer?) {
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
        val gameProfileForUsername = world.server!!.playerProfileCache.getGameProfileForUsername(playerName)
        return gameProfileForUsername?.id
    }

    /**
     * Called to write this tile entities state to NBT data format
     *
     * @param compound The NBT compound to write to
     * @return The NBT compound representing this void chest tile entity
     */
    override fun write(compound: NBTTagCompound): NBTTagCompound {
        // Start by writing any default MC things to the NBT
        super.write(compound)

        // Write the owner as an ID
        if (owner != null) {
            compound.setUniqueId(NBT_OWNER, owner!!)
        }

        // Write the index to go to as an integer
        compound.setInt(NBT_INDEX_TO_GO_TO, indexToGoTo)

        val friendNBT = NBTTagList()
        // For each friend append two new NBTLongs, one for most sig bits and one for least sig bits
        friends.forEach {
            friendNBT.add(NBTTagLong(it.leastSignificantBits))
            friendNBT.add(NBTTagLong(it.mostSignificantBits))
        }

        // Set the friends NBT to be this array
        compound.setTag(NBT_FRIENDS, friendNBT)
        return compound
    }

    /**
     * Reads this tile entity's state in from an NBT compound
     *
     * @param compound The NBT compound to read from
     */
    override fun read(compound: NBTTagCompound) {
        // Start by reading any default MC things from the NBT
        super.read(compound)

        // Read the owner tag, it could potentially not exist...
        owner = if (compound.hasKey(NBT_OWNER + "Most") && compound.hasKey(NBT_OWNER + "Least")) {
            compound.getUniqueId(NBT_OWNER)
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
            if (friendIDLeast is NBTTagLong && friendIDMost is NBTTagLong) {
                // Add the friend back in from least and most significant bits
                friends.add(UUID(friendIDMost.long, friendIDLeast.long))
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