package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncParticle
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint
import kotlin.random.Random

/**
 * Base class for all AOTD effects
 *
 * @param id The id of the spell effect
 * @constructor just calls super currently
 */
abstract class AOTDSpellEffect(id: ResourceLocation) : SpellEffect(id)
{
    companion object
    {
        /**
         * Creates particles at the position. This is static so overridden effects can still use it
         *
         * @param min       The minimum number of particles to spawn
         * @param max       The maximum number of particles to spawn
         * @param pos       The position to spawn particles at
         * @param dimension The dimension to create particles in
         */
        fun createParticlesAt(min: Int, max: Int, pos: Vec3d, dimension: Int)
        {
            // Spawn particles
            val positions = List(Random.nextInt(min, max + 1)) { pos }

            // Send the particle packet
            AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                    SyncParticle(AOTDParticleRegistry.ParticleTypes.SPELL_HIT_ID, positions, List(positions.size) { Vec3d.ZERO }),
                    TargetPoint(dimension, pos.x, pos.y, pos.z, 100.0)
            )
        }
    }
}