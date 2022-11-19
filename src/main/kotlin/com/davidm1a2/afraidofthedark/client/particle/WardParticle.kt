package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.WardParticleData
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f
import kotlin.math.max

class WardParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    entityId: Int,
    private val direction: Direction,
    private val spriteSet: IAnimatedSprite
) : AOTDParticle(world, x, y, z) {
    private val entity = world.getEntity(entityId)

    init {
        // 0.4 second lifespan
        lifetime = 8
        scale(entity?.let { max(it.bbWidth, it.bbHeight) } ?: 2.5f)
        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(spriteSet)
        if (entity == null) {
            val blockState = level.getBlockState(BlockPos(x, y, z).relative(direction.opposite))
            if (blockState.isAir) {
                remove()
            }
        } else if (!entity.isAlive) {
            remove()
        }
    }

    override fun updateMotionXYZ() {
        followEntity()
    }

    private fun followEntity() {
        if (entity != null) {
            setPos(
                entity.x + direction.stepX * entity.bbWidth / 2.0,
                entity.y + entity.bbHeight / 2.0 + direction.stepY * entity.bbHeight / 2.0,
                entity.z + direction.stepZ * entity.bbWidth / 2.0
            )
        }
    }

    override fun render(vertexBuilder: IVertexBuilder, activeRenderInfo: ActiveRenderInfo, partialTicks: Float) {
        // Copy & pasted from super::render except rotation is fixed to a direction

        val vector3d = activeRenderInfo.position
        val x = (MathHelper.lerp(partialTicks.toDouble(), xo, x) - vector3d.x()).toFloat()
        val y = (MathHelper.lerp(partialTicks.toDouble(), yo, y) - vector3d.y()).toFloat()
        val z = (MathHelper.lerp(partialTicks.toDouble(), zo, z) - vector3d.z()).toFloat()
        val quaternion: Quaternion
        if (roll == 0.0f) {
            // Changed this line
            quaternion = direction.rotation.apply { mul(Vector3f.XP.rotationDegrees(90f)) }
        } else {
            // Changed this line
            quaternion = direction.rotation.apply { mul(Vector3f.XP.rotationDegrees(90f)) }
            val f3 = MathHelper.lerp(partialTicks, oRoll, roll)
            quaternion.mul(Vector3f.ZP.rotation(f3))
        }

        val baseRotation = Vector3f(-1.0f, -1.0f, 0.0f)
        baseRotation.transform(quaternion)
        val positions = arrayOf(
            Vector3f(-1.0f, -1.0f, 0.0f),
            Vector3f(-1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f)
        )

        val quadSize = getQuadSize(partialTicks)
        for (i in 0..3) {
            val position = positions[i]
            position.transform(quaternion)
            position.mul(quadSize)
            position.add(x, y, z)
        }

        val lightColor = getLightColor(partialTicks)
        vertexBuilder.vertex(positions[0].x().toDouble(), positions[0].y().toDouble(), positions[0].z().toDouble()).uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexBuilder.vertex(positions[1].x().toDouble(), positions[1].y().toDouble(), positions[1].z().toDouble()).uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexBuilder.vertex(positions[2].x().toDouble(), positions[2].y().toDouble(), positions[2].z().toDouble()).uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexBuilder.vertex(positions[3].x().toDouble(), positions[3].y().toDouble(), positions[3].z().toDouble()).uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<WardParticleData> {
        override fun createParticle(
            particle: WardParticleData,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return WardParticle(world, x, y, z, particle.entityId, particle.direction, spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }
}