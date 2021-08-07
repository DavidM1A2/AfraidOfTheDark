package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacketProcessor
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import org.apache.logging.log4j.LogManager

/**
 * Packet that tells other players in the area that a void chest has been opened
 */
class VoidChestPacketProcessor : EntityPacketProcessor<VoidChestPacket>() {
    override fun encode(msg: VoidChestPacket, buf: PacketBuffer) {
        writeEntityData(msg, buf)
        buf.writeInt(msg.chestX)
        buf.writeInt(msg.chestY)
        buf.writeInt(msg.chestZ)
    }

    override fun decode(buf: PacketBuffer): VoidChestPacket {
        val (uuid, id) = readEntityData(buf)
        return VoidChestPacket(
            uuid,
            id,
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        )
    }

    override fun process(msg: VoidChestPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            // The player that opened the chest
            val chestOpener = player.level.getPlayerByUUID(msg.entityUUID)
            if (chestOpener != null) {
                // Grab the void chest tile entity
                val chestTileEntity = player.level.getBlockEntity(BlockPos(msg.chestX, msg.chestY, msg.chestZ))
                if (chestTileEntity != null) {
                    // Ensure the tile entity is valid
                    if (chestTileEntity is VoidChestTileEntity) {
                        // Open the tile entity
                        chestTileEntity.openChest(chestOpener)
                    } else {
                        logger.warn("Attempted to sync a void chest opening on a non void chest tile entity!")
                    }
                } else {
                    logger.warn("Attempted to find an invalid void chest!")
                }
            } else {
                logger.warn("Attempted to update user of a void chest opening from a null player!")
            }
        }
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}