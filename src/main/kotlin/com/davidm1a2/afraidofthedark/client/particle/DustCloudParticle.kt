package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.level.biome.Biome

class DustCloudParticle(
    world: ClientLevel,
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
        val newScale = Mth.lerp(age.toFloat() / lifetime, minScale, maxScale)
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale)
        quadSize = baseQuadSize * newScale
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()
        yd = yd - 0.03
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            particle: SimpleParticleType,
            world: ClientLevel,
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
                Biome.BiomeCategory.NONE to ParticleColor.GREY,
                Biome.BiomeCategory.TAIGA to ParticleColor.GREEN,
                Biome.BiomeCategory.EXTREME_HILLS to ParticleColor.BROWN,
                Biome.BiomeCategory.JUNGLE to ParticleColor.GREEN,
                Biome.BiomeCategory.MESA to ParticleColor.BROWN,
                Biome.BiomeCategory.SAVANNA to ParticleColor.BROWN,
                Biome.BiomeCategory.ICY to ParticleColor.GREY,
                Biome.BiomeCategory.THEEND to ParticleColor.GREY,
                Biome.BiomeCategory.BEACH to ParticleColor.BLUE,
                Biome.BiomeCategory.FOREST to ParticleColor.GREEN,
                Biome.BiomeCategory.OCEAN to ParticleColor.BLUE,
                Biome.BiomeCategory.DESERT to ParticleColor.BROWN,
                Biome.BiomeCategory.RIVER to ParticleColor.BLUE,
                Biome.BiomeCategory.SWAMP to ParticleColor.GREEN,
                Biome.BiomeCategory.MUSHROOM to ParticleColor.BROWN,
                Biome.BiomeCategory.NETHER to ParticleColor.GREY
            )
        }
    }
}