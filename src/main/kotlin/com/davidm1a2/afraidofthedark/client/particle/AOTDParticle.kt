package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.particle.SpriteTexturedParticle
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Base class for all AOTD particles
 *
 * @constructor Constructor sets all of the particle's properties
 * @param world The world the sprite is in
 * @param x       The x position of the particle
 * @param y       The y position of the particle
 * @param z       The z position of the particle
 * @param xSpeed  The x speed of the particle
 * @param ySpeed  The y speed of the particle
 * @param zSpeed  The z speed of the particle
 */
@OnlyIn(Dist.CLIENT)
abstract class AOTDParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double = 0.0,
    ySpeed: Double = 0.0,
    zSpeed: Double = 0.0
) : SpriteTexturedParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    override fun getRenderType(): IParticleRenderType {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    /**
     * Copied code from base particle with modifications to update motion x,y,z
     */
    override fun tick() {
        // Update the previous positions to be the current position
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ

        // If the particle is too old kill it off
        if (age++ >= maxAge) {
            setExpired()
        }

        // Update the x,y,z motion
        updateMotionXYZ()

        // Move the particle based on motion
        move(motionX, motionY, motionZ)

        // If the particle is on the ground reduce the motion quickly
        if (onGround) {
            motionX *= 0.7
            motionZ *= 0.7
        }
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    open fun updateMotionXYZ() {
        // Default: Don't update motion at all, let it remain constant
    }
}