package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.biome.Biome

class DustCloudParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    private val minScale = 1f + random.nextFloat() * 0.5f
    private val maxScale = 4.5f + minScale
    private val baseQuadSize: Float

    init {
        lifetime = random.nextInt(5) + 20

        scale(minScale)
        baseQuadSize = quadSize
    }

    override fun tick() {
        super.tick()
        alpha = alpha * if (age < lifetime - 5) 0.95f else 0.5f

        // Expand the particle over time
        val newScale = MathHelper.lerp(age.toFloat() / lifetime, minScale, maxScale)
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale)
        quadSize = baseQuadSize * newScale
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()
        yd = yd - 0.03
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun createParticle(
            particle: BasicParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            // Color the particle based on biome
            val biome = world.getBiome(BlockPos(x, y, z))
            val color = CATEGORY_TO_COLOR[biome.biomeCategory] ?: ParticleColor.GREY
            return DustCloudParticle(world, x, y, z, xSpeed, ySpeed, zSpeed).apply {
                setSprite(spriteSet.get(color.index, ParticleColor.values().size))
            }
        }

        private enum class ParticleColor(val index: Int) {
            BLUE(1),
            BROWN(2),
            GREEN(3),
            GREY(4)
        }

        companion object {
            private val CATEGORY_TO_COLOR = mapOf(
                Biome.Category.NONE to ParticleColor.GREY,
                Biome.Category.TAIGA to ParticleColor.GREEN,
                Biome.Category.EXTREME_HILLS to ParticleColor.BROWN,
                Biome.Category.JUNGLE to ParticleColor.GREEN,
                Biome.Category.MESA to ParticleColor.BROWN,
                Biome.Category.SAVANNA to ParticleColor.BROWN,
                Biome.Category.ICY to ParticleColor.GREY,
                Biome.Category.THEEND to ParticleColor.GREY,
                Biome.Category.BEACH to ParticleColor.BLUE,
                Biome.Category.FOREST to ParticleColor.GREEN,
                Biome.Category.OCEAN to ParticleColor.BLUE,
                Biome.Category.DESERT to ParticleColor.BROWN,
                Biome.Category.RIVER to ParticleColor.BLUE,
                Biome.Category.SWAMP to ParticleColor.GREEN,
                Biome.Category.MUSHROOM to ParticleColor.BROWN,
                Biome.Category.NETHER to ParticleColor.GREY
            )
        }
    }
}