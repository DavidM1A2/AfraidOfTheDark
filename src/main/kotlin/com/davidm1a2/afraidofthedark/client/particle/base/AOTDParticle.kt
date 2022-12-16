package com.davidm1a2.afraidofthedark.client.particle.base

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.particle.SpriteTexturedParticle
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.world.ClientWorld

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
abstract class AOTDParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double = 0.0,
    ySpeed: Double = 0.0,
    zSpeed: Double = 0.0
) : SpriteTexturedParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    init {
        quadSize = 0.2f
        xd = xSpeed
        yd = ySpeed
        zd = zSpeed
    }

    override fun getRenderType(): IParticleRenderType {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    /**
     * Copied code from base particle with modifications to update motion x,y,z
     */
    override fun tick() {
        // Update the previous positions to be the current position
        xo = x
        yo = y
        zo = z

        // If the particle is too old kill it off
        if (age++ >= lifetime) {
            remove()
        }

        // Update the x,y,z motion
        updateMotionXYZ()

        // Move the particle based on motion
        move(xd, yd, zd)

        // If the particle is on the ground reduce the motion quickly
        if (onGround) {
            xd *= 0.7
            zd *= 0.7
        }
    }

    fun setAlphaFadeInLastTicks(ticksToFade: Float) {
        alpha = if (lifetime - age < ticksToFade) {
            (lifetime - age) / ticksToFade
        } else {
            1f
        }
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    open fun updateMotionXYZ() {
        // Default: Don't update motion at all, let it remain constant
    }

    companion object {
        // Copied and pasted from IParticleRenderType PARTICLE_SHEET_TRANSLUCENT, but without culling
        internal val PARTICLE_SHEET_TRANSLUCENT_NO_CULL = object : IParticleRenderType {
            override fun begin(bufferBuilder: BufferBuilder, textureManager: TextureManager) {
                RenderSystem.disableCull()
                IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT.begin(bufferBuilder, textureManager)
            }

            override fun end(tessellator: Tessellator) {
                IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT.end(tessellator)
                RenderSystem.enableCull()
            }

            override fun toString(): String {
                return "PARTICLE_SHEET_TRANSLUCENT_NO_CULL"
            }
        }

        // Copied and pasted from IParticleRenderType PARTICLE_SHEET_TRANSLUCENT_NO_CULL, but without color depth buffer
        internal val PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH_MASK = object : IParticleRenderType {
            override fun begin(bufferBuilder: BufferBuilder, textureManager: TextureManager) {
                // This enables the depth mask, so disable after this call
                PARTICLE_SHEET_TRANSLUCENT_NO_CULL.begin(bufferBuilder, textureManager)
                RenderSystem.depthMask(false)
            }

            override fun end(tessellator: Tessellator) {
                PARTICLE_SHEET_TRANSLUCENT_NO_CULL.end(tessellator)
                RenderSystem.depthMask(true)
            }

            override fun toString(): String {
                return "PARTICLE_SHEET_TRANSLUCENT_NO_COLOR_MASK"
            }
        }
    }
}