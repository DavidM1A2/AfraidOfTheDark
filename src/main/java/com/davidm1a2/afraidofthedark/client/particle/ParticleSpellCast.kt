package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle representing a player's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast effect
 * @param y The y position of the spell cast effect
 * @param z The z position of the spell cast effect
 */
class ParticleSpellCast(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, ModSprites.SPELL_CAST, x, y, z)
{
    init
    {
        // 0.5-1.5 second lifespan
        particleMaxAge = rand.nextInt(10) + 30
        // Make the particles noticable
        particleScale = 2.0f + rand.nextFloat() * 2
        // Random motion
        motionX = (rand.nextFloat() - 0.5) * 0.2
        motionY = rand.nextFloat() * 0.1
        motionZ = (rand.nextFloat() - 0.5) * 0.2
    }
}