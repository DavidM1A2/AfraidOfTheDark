package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * A special particle spell hit effect
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell hit effect
 * @param y The y position of the spell hit effect
 * @param z The z position of the spell hit effect
 */
class ParticleSpellHit(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, ModSprites.SPELL_HIT, x, y, z)
{
    init
    {
        // 0.5-1.5 second lifespan
        particleMaxAge = rand.nextInt(30) + 10
        // Make the particles noticable
        particleScale = 1.0f + rand.nextFloat() * 4
        // Random motion
        motionX = (rand.nextFloat() - 0.5) * 0.05
        motionY = rand.nextFloat() * 0.02
        motionZ = (rand.nextFloat() - 0.5) * 0.05
    }
}