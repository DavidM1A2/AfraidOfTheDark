package com.DavidM1A2.afraidofthedark.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

/**
 * Base class for all AOTD particles
 */
public abstract class AOTDParticle extends Particle
{
    /**
     * Constructor sets all of the particle's properties
     *
     * @param worldIn The world the sprite is in
     * @param sprite  The sprite containing the particle texture
     * @param x       The x position of the particle
     * @param y       The y position of the particle
     * @param z       The z position of the particle
     * @param xSpeed  The x speed of the particle
     * @param ySpeed  The y speed of the particle
     * @param zSpeed  The z speed of the particle
     */
    public AOTDParticle(World worldIn, TextureAtlasSprite sprite, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
    {
        super(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setParticleTexture(sprite);
    }

    /**
     * @return Must be 1 for custom textures
     */
    @Override
    public int getFXLayer()
    {
        return 1;
    }

    /**
     * Copied code from base particle with modifications to update motion x,y,z
     */
    @Override
    public void onUpdate()
    {
        // Update the previous positions to be the current position
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        // If the particle is too old kill it off
        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        // Update the x,y,z motion
        this.updateMotionXYZ();
        // Move the particle based on motion
        this.move(this.motionX, this.motionY, this.motionZ);

        // If the particle is on the ground reduce the motion quickly
        if (this.onGround)
        {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
        }
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    abstract void updateMotionXYZ();
}
