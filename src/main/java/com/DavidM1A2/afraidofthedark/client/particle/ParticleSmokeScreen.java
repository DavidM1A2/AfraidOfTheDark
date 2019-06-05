package com.DavidM1A2.afraidofthedark.client.particle;

import com.DavidM1A2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.world.World;

/**
 * Particle used to create a smoke screen
 */
public class ParticleSmokeScreen extends AOTDParticle
{
    /**
     * Constructor takes the x,y,z position of the particle and the world
     *
     * @param worldIn  The world the particle is at
     * @param xCoordIn The x position of the basic attack
     * @param yCoordIn The y position of the basic attack
     * @param zCoordIn The z position of the basic attack
     */
    public ParticleSmokeScreen(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, ModSprites.SMOKE_SCREEN, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
        // 10-20 second lifespan
        this.particleMaxAge = this.rand.nextInt(200) + 200;
        // Blinding size teleport particles
        this.particleScale = 3f + this.rand.nextFloat() * 13f;
        // Particle moves outwards
        this.motionX = this.rand.nextFloat() * 0.07;
        this.motionX = this.rand.nextBoolean() ? -this.motionX - 0.07 : this.motionX + 0.07;
        this.motionY = this.rand.nextFloat() * 0.07;
        this.motionY = this.rand.nextBoolean() ? -this.motionY - 0.07 : this.motionY + 0.07;
        this.motionZ = this.rand.nextFloat() * 0.07;
        this.motionZ = this.rand.nextBoolean() ? -this.motionZ - 0.07 : this.motionZ + 0.07;
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    @Override
    void updateMotionXYZ()
    {
        // Slowly reduce motion
        this.motionX = this.motionX * 0.95;
        this.motionY = this.motionY * 0.95;
        this.motionZ = this.motionZ * 0.95;
    }
}
