package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.AOTDParticleType
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.PacketDistributor
import kotlin.random.Random

/**
 * Base class for all AOTD effects
 *
 * @param id The id of the spell effect
 * @param prerequisiteResearch The research required to use this component, or null if none is required
 * @constructor just calls super currently
 */
abstract class AOTDSpellEffect(id: ResourceLocation, prerequisiteResearch: Research? = null) : SpellEffect(id, prerequisiteResearch) {
    companion object {
        /**
         * Creates particles at the position. This is static so overridden effects can still use it
         *
         * @param min       The minimum number of particles to spawn
         * @param max       The maximum number of particles to spawn
         * @param pos       The position to spawn particles at
         * @param dimension The dimension to create particles in
         */
        fun createParticlesAt(min: Int, max: Int, pos: Vector3d, dimension: RegistryKey<World>, particleType: AOTDParticleType) {
            // Spawn particles
            val positions = List(Random.nextInt(min, max + 1)) { pos }

            if (positions.isNotEmpty()) {
                // Send the particle packet
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    ParticlePacket(
                        particleType,
                        positions,
                        List(positions.size) { Vector3d.ZERO }),
                    PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 100.0, dimension)
                )
            }
        }

        fun createParticlesAround(min: Int, max: Int, pos: Vector3d, dimension: RegistryKey<World>, particleType: AOTDParticleType, maxDistance: Double) {
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
                        List(positions.size) { Vector3d.ZERO }),
                    PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 100.0, dimension)
                )
            }
        }
    }
}