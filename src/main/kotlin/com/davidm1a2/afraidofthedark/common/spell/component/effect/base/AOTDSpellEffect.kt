package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.particles.IParticleData
import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.PacketDistributor
import kotlin.random.Random

abstract class AOTDSpellEffect(name: String, prerequisiteResearch: Research? = null) : SpellEffect(ResourceLocation(Constants.MOD_ID, name), prerequisiteResearch) {
    companion object {
        /**
         * Creates particles at the position. This is static so overridden effects can still use it
         *
         * @param min       The minimum number of particles to spawn
         * @param max       The maximum number of particles to spawn
         * @param pos       The position to spawn particles at
         * @param dimension The dimension to create particles in
         */
        fun createParticlesAt(min: Int, max: Int, pos: Vector3d, dimension: RegistryKey<World>, particleType: IParticleData) {
            // Spawn particles
            val positions = List(Random.nextInt(min, max + 1)) { pos }

            // Send the particle packet
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(particleType)
                    .positions(positions)
                    .speed(Vector3d.ZERO)
                    .build(),
                PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 100.0, dimension)
            )
        }

        fun createParticlesAround(min: Int, max: Int, pos: Vector3d, dimension: RegistryKey<World>, particleType: IParticleData, maxDistance: Double) {
            // Spawn particles
            val positions = List(Random.nextInt(min, max + 1)) {
                pos.add(
                    Random.nextDouble(-maxDistance, maxDistance),
                    Random.nextDouble(-maxDistance, maxDistance),
                    Random.nextDouble(-maxDistance, maxDistance)
                )
            }

            // Send the particle packet
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(particleType)
                    .positions(positions)
                    .speed(Vector3d.ZERO)
                    .build(),
                PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 100.0, dimension)
            )
        }
    }
}