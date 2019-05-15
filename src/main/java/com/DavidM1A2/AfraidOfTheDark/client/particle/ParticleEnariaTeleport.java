package com.DavidM1A2.afraidofthedark.client.particle;

import com.DavidM1A2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.world.World;

/**
 * Particle representing enaria's teleport
 */
public class ParticleEnariaTeleport extends AOTDParticle
{
    /**
     * Constructor takes the x,y,z position of the particle and the world
     *
     * @param worldIn  The world the particle is at
     * @param xCoordIn The x position of the basic attack
     * @param yCoordIn The y position of the basic attack
     * @param zCoordIn The z position of the basic attack
     */
    public ParticleEnariaTeleport(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, ModSprites.ENARIA_TELEPORT, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
        // 2-3 second lifespan
        this.particleMaxAge = this.rand.nextInt(20) + 40;
        // Blinding size teleport particles
        this.particleScale = 10.0f + this.rand.nextFloat() * 5;
        // Upwards motion only
        this.motionX = 0;
        this.motionY = this.rand.nextFloat() * 0.1 + 0.3;
        this.motionZ = 0;
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    @Override
    void updateMotionXYZ()
    {
        // Don't update motion at all, let it remain constant
    }
}
