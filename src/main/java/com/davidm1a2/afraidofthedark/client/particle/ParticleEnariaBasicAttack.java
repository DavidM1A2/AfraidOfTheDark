package com.davidm1a2.afraidofthedark.client.particle;

import com.davidm1a2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.world.World;

/**
 * Particle representing enaria's basic attack
 */
public class ParticleEnariaBasicAttack extends AOTDParticle
{
    /**
     * Constructor takes the x,y,z position of the particle and the world
     *
     * @param worldIn  The world the particle is at
     * @param xCoordIn The x position of the basic attack
     * @param yCoordIn The y position of the basic attack
     * @param zCoordIn The z position of the basic attack
     */
    public ParticleEnariaBasicAttack(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, ModSprites.ENARIA_BASIC_ATTACK, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
        // 1-2 second lifespan
        this.particleMaxAge = this.rand.nextInt(20) + 20;
        // No motion
        this.motionX = 0;
        this.motionY = 0;
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
