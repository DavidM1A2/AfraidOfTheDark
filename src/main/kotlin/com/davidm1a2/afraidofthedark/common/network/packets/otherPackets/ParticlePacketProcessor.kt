package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.registries.ForgeRegistries

/**
 * Custom packet used to make the client create a particle effect
 */
class ParticlePacketProcessor : PacketProcessor<ParticlePacket> {
    override fun encode(msg: ParticlePacket, buf: PacketBuffer) {
        // Write the particle index first, then the number of particles to spawn, then each position and speed
        buf.writeResourceLocation(msg.particle.type.registryName!!)
        msg.particle.write(buf)
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
        @Suppress("UNCHECKED_CAST")
        val particleType = ForgeRegistries.PARTICLE_TYPES.getValue(buf.readResourceLocation()) as ParticleType<IParticleData>
        val particle = particleType.deserializer.read(particleType, buf)

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
                player.world.addParticle(msg.particle, false, position.x, position.y, position.z, speed.x, speed.y, speed.z)
            }
        }
    }
}