package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent from a client to the server that updates the number of ticks the player will be frozen for
 */
class SpellFreezeDataPacketProcessor : PacketProcessor<SpellFreezeDataPacket> {
    override fun encode(msg: SpellFreezeDataPacket, buf: PacketBuffer) {
        buf.writeInt(msg.freezeTicks)
        if (msg.freezeTicks > 0) {
            buf.writeDouble(msg.position!!.x)
            buf.writeDouble(msg.position.y)
            buf.writeDouble(msg.position.z)
            buf.writeFloat(msg.yaw)
            buf.writeFloat(msg.pitch)
        }
    }

    override fun decode(buf: PacketBuffer): SpellFreezeDataPacket {
        val freezeTicks = buf.readInt()
        return if (freezeTicks > 0) {
            SpellFreezeDataPacket(
                freezeTicks,
                Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat(),
                buf.readFloat()
            )
        } else {
            SpellFreezeDataPacket(freezeTicks, null, 0.0f, 0.0f)
        }
    }

    override fun process(msg: SpellFreezeDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val freezeData = Minecraft.getInstance().player!!.getSpellFreezeData()
            freezeData.freezeTicks = msg.freezeTicks
            freezeData.freezePosition = msg.position
            freezeData.freezePitch = msg.pitch
            freezeData.freezeYaw = msg.yaw
        }
    }
}