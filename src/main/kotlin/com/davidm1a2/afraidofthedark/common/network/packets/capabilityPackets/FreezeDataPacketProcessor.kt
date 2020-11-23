package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent from a client to the server that updates the number of ticks the player will be frozen for
 */
class FreezeDataPacketProcessor : PacketProcessor<FreezeDataPacket> {
    override fun encode(msg: FreezeDataPacket, buf: PacketBuffer) {
        buf.writeInt(msg.freezeTicks)
        if (msg.freezeTicks > 0) {
            buf.writeDouble(msg.position!!.x)
            buf.writeDouble(msg.position.y)
            buf.writeDouble(msg.position.z)
            buf.writeFloat(msg.yaw)
            buf.writeFloat(msg.pitch)
        }
    }

    override fun decode(buf: PacketBuffer): FreezeDataPacket {
        val freezeTicks = buf.readInt()
        return if (freezeTicks > 0) {
            FreezeDataPacket(
                freezeTicks,
                Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat(),
                buf.readFloat()
            )
        } else {
            FreezeDataPacket(freezeTicks, null, 0.0f, 0.0f)
        }
    }

    override fun process(msg: FreezeDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val freezeData = Minecraft.getInstance().player.getSpellFreezeData()
            freezeData.freezeTicks = msg.freezeTicks
            freezeData.freezePosition = msg.position
            freezeData.setFreezeDirection(msg.yaw, msg.pitch)
        }
    }
}