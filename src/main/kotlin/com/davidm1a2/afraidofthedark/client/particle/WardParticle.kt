package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.RotatedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.WardParticleData
import com.mojang.math.Vector3f
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

class WardParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    scale: Float,
    private val direction: Direction,
    private val spriteSet: SpriteSet
) : RotatedAOTDParticle(world, x, y, z, direction.rotation.apply { mul(Vector3f.XP.rotationDegrees(90f)) }) {
    init {
        // 0.4 second lifespan
        lifetime = 8
        scale(scale)
        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(spriteSet)
        val blockState = level.getBlockState(BlockPos(x, y, z).relative(direction.opposite))
        if (blockState.isAir) {
            remove()
        }
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<WardParticleData> {
        override fun createParticle(
            particle: WardParticleData,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return WardParticle(world, x, y, z, particle.scale, particle.direction, spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }
}