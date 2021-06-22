package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.AOTDParticleType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.fml.network.PacketDistributor
import kotlin.random.Random

/**
 * Base class for all AOTD effects
 *
 * @param id The id of the spell effect
 * @constructor just calls super currently
 */
abstract class AOTDSpellEffect(id: ResourceLocation) : SpellEffect(id) {
    companion object {
        /**
         * Creates particles at the position. This is static so overridden effects can still use it
         *
         * @param min       The minimum number of particles to spawn
         * @param max       The maximum number of particles to spawn
         * @param pos       The position to spawn particles at
         * @param dimension The dimension to create particles in
         */
        fun createParticlesAt(min: Int, max: Int, pos: Vec3d, dimension: DimensionType, particleType: AOTDParticleType) {
            // Spawn particles
            val positions = List(Random.nextInt(min, max + 1)) { pos }

            if (positions.isNotEmpty()) {
                // Send the particle packet
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    ParticlePacket(
                        particleType,
                        positions,
                        List(positions.size) { Vec3d.ZERO }),
                    PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 100.0, dimension)
                )
            }
        }

        fun createParticlesAround(min: Int, max: Int, pos: Vec3d, dimension: DimensionType, particleType: AOTDParticleType, maxDistance: Double) {
            // Spawn particles
            val positions = List(Random.nextInt(min, max + 1)) {
                pos.add(
                    Random.nextDouble(-maxDistance, maxDistance),
                    Random.nextDouble(-maxDistance, maxDistance),
                    Random.nextDouble(-maxDistance, maxDistance)
                )
            }

            if (positions.isNotEmpty()) {
                // Send the particle packet
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    ParticlePacket(
                        particleType,
                        positions,
                        List(positions.size) { Vec3d.ZERO }),
                    PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 100.0, dimension)
                )
            }
        }
    }
}