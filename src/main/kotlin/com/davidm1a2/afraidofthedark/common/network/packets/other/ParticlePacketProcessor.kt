package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistry
import kotlin.math.max

/**
 * Custom packet used to make the client create a particle effect
 */
class ParticlePacketProcessor : PacketProcessor<ParticlePacket> {
    override fun encode(msg: ParticlePacket, buf: PacketBuffer) {
        // Write the particle index first, then the number of particles to spawn, then each position and speed
        buf.writeInt(msg.particles.size)
        buf.writeInt(msg.positions.size)
        buf.writeInt(msg.speeds.size)

        for (particle in msg.particles) {
            buf.writeInt(PARTICLE_TYPES.getID(particle.type))
            particle.writeToNetwork(buf)
        }

        for (position in msg.positions) {
            buf.writeDouble(position.x)
            buf.writeDouble(position.y)
            buf.writeDouble(position.z)
        }

        for (speed in msg.speeds) {
            buf.writeDouble(speed.x)
            buf.writeDouble(speed.y)
            buf.writeDouble(speed.z)
        }
    }

    override fun decode(buf: PacketBuffer): ParticlePacket {
        val numParticles = buf.readInt()
        val numPositions = buf.readInt()
        val numSpeeds = buf.readInt()

        val particles = mutableListOf<IParticleData>()
        val positions = mutableListOf<Vector3d>()
        val speeds = mutableListOf<Vector3d>()

        for (ignored in 0 until numParticles) {
            @Suppress("UNCHECKED_CAST")
            val particleType = PARTICLE_TYPES.getValue(buf.readInt()) as ParticleType<IParticleData>
            particles.add(particleType.deserializer.fromNetwork(particleType, buf))
        }

        for (ignored in 0 until numPositions) {
            positions.add(Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()))
        }

        for (ignored in 0 until numSpeeds) {
            speeds.add(Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()))
        }

        return ParticlePacket.builder()
            .particles(particles)
            .positions(positions)
            .speeds(speeds)
            .build()
    }

    override fun process(msg: ParticlePacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Grab the list of positions and speeds
            val particles = msg.particles
            val positions = msg.positions
            val speeds = msg.speeds
            val particleCount = max(particles.size, max(positions.size, speeds.size))

            // Go over each position and speed and spawn a particle for it
            val player = Minecraft.getInstance().player!!
            for (i in 0 until particleCount) {
                val particle = particles.getOrElse(i) { particles[0] }
                val position = positions.getOrElse(i) { positions[0] }
                val speed = speeds.getOrElse(i) { speeds[0] }
                player.level.addParticle(particle, false, position.x, position.y, position.z, speed.x, speed.y, speed.z)
            }
        }
    }

    companion object {
        // Need a "ForgeRegistry" not an "IForgeRegistry" to get access to registry IDs
        private val PARTICLE_TYPES = ForgeRegistries.PARTICLE_TYPES as ForgeRegistry<ParticleType<*>>
    }
}