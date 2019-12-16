package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle representing enaria's basic attack
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the basic attack
 * @param y The y position of the basic attack
 * @param z The z position of the basic attack
 */
class ParticleEnariaBasicAttack(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, ModSprites.ENARIA_BASIC_ATTACK, x, y, z)
{
    init
    {
        // 1-2 second lifespan
        particleMaxAge = rand.nextInt(20) + 20
        // No motion
        motionX = 0.0
        motionY = 0.0
        motionZ = 0.0
    }
}