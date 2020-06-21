package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry.ParticleTypes
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry.spawnParticle
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Custom packet used to make the client create a particle effect
 */
class ParticlePacketProcessor : PacketProcessor<ParticlePacket> {
    override fun encode(msg: ParticlePacket, buf: PacketBuffer) {
        // Write the particle index first, then the number of particles to spawn, then each position and speed
        buf.writeInt(msg.particle.ordinal)
        buf.writeInt(msg.positions.size)
        for (i in msg.positions.indices) {
            buf.writeDouble(msg.positions[i].x)
            buf.writeDouble(msg.positions[i].y)
            buf.writeDouble(msg.positions[i].z)
            buf.writeDouble(msg.speeds[i].x)
            buf.writeDouble(msg.speeds[i].y)
            buf.writeDouble(msg.speeds[i].z)
        }
    }

    override fun decode(buf: PacketBuffer): ParticlePacket {
        // Read the particle, then the number of particles to spawn, then the position/speed of each particle
        val particle = ParticleTypes.values()[buf.readInt()]

        val numPositions = buf.readInt()
        val mutablePositions = mutableListOf<Vec3d>()
        val mutableSpeeds = mutableListOf<Vec3d>()
        for (i in 0 until numPositions) {
            mutablePositions.add(Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()))
            mutableSpeeds.add(Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()))
        }

        return ParticlePacket(
            particle,
            mutablePositions.toList(),
            mutableSpeeds.toList()
        )
    }

    override fun process(msg: ParticlePacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Grab the list of positions and speeds
            val positions = msg.positions
            val speeds = msg.speeds

            // Go over each position and speed and spawn a particle for it
            val player = Minecraft.getInstance().player
            for (i in positions.indices) {
                val position = positions[i]
                val speed = speeds[i]
                spawnParticle(msg.particle, player.world, position.x, position.y, position.z, speed.x, speed.y, speed.z)
            }
        }
    }
}