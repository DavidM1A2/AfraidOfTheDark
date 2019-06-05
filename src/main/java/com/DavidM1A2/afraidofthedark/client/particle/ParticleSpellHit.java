package com.DavidM1A2.afraidofthedark.client.particle;

import com.DavidM1A2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.world.World;

/**
 * A special particle spell hit effect
 */
public class ParticleSpellHit extends AOTDParticle
{
    /**
     * Constructor takes the x,y,z position of the particle and the world
     *
     * @param worldIn  The world the particle is at
     * @param xCoordIn The x position of the basic attack
     * @param yCoordIn The y position of the basic attack
     * @param zCoordIn The z position of the basic attack
     */
    public ParticleSpellHit(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, ModSprites.SPELL_HIT, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
        // 0.5-1.5 second lifespan
        this.particleMaxAge = this.rand.nextInt(30) + 10;
        // Make the particles noticable
        this.particleScale = 1.0f + this.rand.nextFloat() * 4;
        // Random motion
        this.motionX = (this.rand.nextFloat() - 0.5) * 0.05;
        this.motionY = this.rand.nextFloat() * 0.02;
        this.motionZ = (this.rand.nextFloat() - 0.5) * 0.05;
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    @Override
    void updateMotionXYZ()
    {
        // Don't update motion
    }
}
