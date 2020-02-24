package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.packets.EntitySyncBase
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packet that tells other players in the area that a void chest has been opened
 *
 * @property chestX The chest's x position
 * @property chestY The chest's y position
 * @property chestZ The chest's z position
 */
class SyncVoidChest : EntitySyncBase {
    private var chestX: Int
    private var chestY: Int
    private var chestZ: Int

    /**
     * Default constructor is required but not used
     */
    constructor() : super() {
        chestX = 0
        chestY = 0
        chestZ = 0
    }

    /**
     * Primary constructor initializes fields
     *
     * @param chestX The X position of this chest
     * @param chestY The Y position of this chest
     * @param chestZ The Z position of this chest
     * @param player The player that opened the chest
     */
    constructor(chestX: Int, chestY: Int, chestZ: Int, player: EntityPlayer) : super(player) {
        this.chestX = chestX
        this.chestY = chestY
        this.chestZ = chestZ
    }

    /**
     * Deserializes the UUID and chest x,y,z
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf) {
        super.fromBytes(buf)
        chestX = buf.readInt()
        chestY = buf.readInt()
        chestZ = buf.readInt()
    }

    /**
     * Serializes the UUID and chest x,y,z
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf) {
        super.toBytes(buf)
        buf.writeInt(chestX)
        buf.writeInt(chestY)
        buf.writeInt(chestZ)
    }

    /**
     * Handler class handles SyncVoidChest packets on the client side
     */
    class Handler : MessageHandler.Client<SyncVoidChest>() {
        /**
         * Called to handle the packet on the client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent from
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncVoidChest, ctx: MessageContext) {
            // The player that opened the chest
            val chestOpener = player.world.getPlayerEntityByUUID(msg.entityUUID)
            if (chestOpener != null) {
                // Grab the void chest tile entity
                val chestTileEntity = player.world.getTileEntity(BlockPos(msg.chestX, msg.chestY, msg.chestZ))
                if (chestTileEntity != null) {
                    // Ensure the tile entity is valid
                    if (chestTileEntity is TileEntityVoidChest) {
                        // Open the tile entity
                        chestTileEntity.openChest(chestOpener)
                    } else {
                        AfraidOfTheDark.INSTANCE.logger.warn("Attempted to sync a void chest opening on a non void chest tile entity!")
                    }
                } else {
                    AfraidOfTheDark.INSTANCE.logger.warn("Attempted to find an invalid void chest!")
                }
            } else {
                AfraidOfTheDark.INSTANCE.logger.warn("Attempted to update user of a void chest opening from a null player!")
            }
        }
    }
}