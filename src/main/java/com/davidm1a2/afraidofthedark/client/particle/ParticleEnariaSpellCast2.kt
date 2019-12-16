package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle representing the second part of enaria's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast attack
 * @param y The y position of the spell cast attack
 * @param z The z position of the spell cast attack
 * @param xSpeed The x speed of the spell cast attack
 * @param zSpeed The z speed of the spell cast attack
 */
class ParticleEnariaSpellCast2(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, ModSprites.ENARIA_SPELL_CAST_2, x, y, z, xSpeed, 0.0, zSpeed)
{
    init
    {
        // 2-3 second lifespan
        particleMaxAge = rand.nextInt(20) + 40
        // Make the particles huge when she casts a spell
        particleScale = 5.0f
        // speed will be the same as motion for this particle
        motionX = xSpeed
        motionY = 0.0
        motionZ = zSpeed
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ()
    {
        // Slowly increase y motion
        motionY = motionY - 0.02
    }
}