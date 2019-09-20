package com.davidm1a2.afraidofthedark.client.particle;

import com.davidm1a2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.world.World;

/**
 * Particle representing the laser delivery spell's laser
 */
public class ParticleSpellLaser extends AOTDParticle
{
    /**
     * Constructor takes the x,y,z position of the particle and the world
     *
     * @param worldIn  The world the particle is at
     * @param xCoordIn The x position of the basic attack
     * @param yCoordIn The y position of the basic attack
     * @param zCoordIn The z position of the basic attack
     */
    public ParticleSpellLaser(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, ModSprites.SPELL_LASER, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
        // 1 second lifespan
        this.particleMaxAge = 20;
        // No motion
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        // Scale starts at 1.0
        this.particleScale = 1.0f;
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    @Override
    void updateMotionXYZ()
    {
        // Don't update motion at all, let it remain constant

        // Set scale to be based on time alive
        this.particleScale = ((float) this.particleMaxAge - (float) this.particleAge) / (float) this.particleMaxAge;
    }
}
