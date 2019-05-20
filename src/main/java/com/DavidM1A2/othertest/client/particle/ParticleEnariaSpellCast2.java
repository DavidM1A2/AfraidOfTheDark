package com.DavidM1A2.afraidofthedark.client.particle;

import com.DavidM1A2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.world.World;

/**
 * Particle representing the second part of enaria's spell cast
 */
public class ParticleEnariaSpellCast2 extends AOTDParticle
{
    /**
     * Constructor takes the x,y,z position of the particle and the world
     *
     * @param worldIn  The world the particle is at
     * @param xCoordIn The x position of the basic attack
     * @param yCoordIn The y position of the basic attack
     * @param zCoordIn The z position of the basic attack
     */
    public ParticleEnariaSpellCast2(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double zSpeedIn)
    {
        super(worldIn, ModSprites.ENARIA_SPELL_CAST_2, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, 0, zSpeedIn);
        // 2-3 second lifespan
        this.particleMaxAge = this.rand.nextInt(20) + 40;
        // Make the particles huge when she casts a spell
        this.particleScale = 5.0f;
        // speed will be the same as motion for this particle
        this.motionX = xSpeedIn;
        this.motionY = 0;
        this.motionZ = zSpeedIn;
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    @Override
    void updateMotionXYZ()
    {
        // Slowly increase y motion
        this.motionY = this.motionY - 0.02;
    }
}
