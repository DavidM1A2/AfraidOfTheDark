package com.davidm1a2.afraidofthedark.client.entity.spell

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.computeRotationTo
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.getOrthogonalVectors
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellChainEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderState.TransparencyState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f
import org.lwjgl.opengl.GL11
import kotlin.math.ceil
import kotlin.math.log
import kotlin.random.Random

class SpellChainRenderer(renderManager: EntityRendererManager) : EntityRenderer<SpellChainEntity>(renderManager) {
    override fun render(
        spellChain: SpellChainEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        // How to make good looking lightning: https://developer.download.nvidia.com/SDK/10/direct3d/Source/Lightning/doc/lightning_doc.pdf
        val buffer = renderTypeBuffer.getBuffer(RENDER_TYPE)
        val startPos = spellChain.position()
        val endPos = spellChain.endPos
        if (startPos == endPos) {
            return
        }

        // Seed the RNG object with the spell object's UUID, so it looks different each time. Change
        // the seed every 2 ticks, so the lightning changes shape rapidly
        val random = Random(spellChain.uuid.mostSignificantBits.toInt() + spellChain.tickCount / 2)

        val distance = startPos.distanceTo(endPos)
        val numJitters = distance / APPROXIMATE_BLOCKS_PER_JITTER
        val numJitterSubdivisions = ceil(log(numJitters, 2.0)).toInt()
        val jitterVertices = mutableListOf<Vector3d>(startPos)
        computeJitters(startPos, endPos, random, jitterVertices, numJitterSubdivisions)
        jitterVertices.add(endPos)
        val forks = computeForks(random, jitterVertices)

        matrixStack.pushPose()
        for (i in 1 until jitterVertices.size) {
            val fromJitterVertex = jitterVertices[i - 1]
            val toJitterVertex = jitterVertices[i]

            // Draw the fork first if it exists for this vertex
            val forkVertices = forks[fromJitterVertex]
            if (forkVertices != null) {
                matrixStack.pushPose()
                for (j in 1 until forkVertices.size) {
                    val fromForkVertex = forkVertices[j - 1]
                    val toForkVertex = forkVertices[j]
                    drawSegment(matrixStack, buffer, fromForkVertex, toForkVertex, FORK_WIDTH)
                }
                matrixStack.popPose()
            }

            // Then draw the jitter
            drawSegment(matrixStack, buffer, fromJitterVertex, toJitterVertex, JITTER_WIDTH)
        }
        matrixStack.popPose()
    }

    /**
     * Algorithm computes random jitters to split the main segment path. It recursively computes jitters on subcomponents and returns
     * its output in the "jitter vertices" list.
     */
    private fun computeJitters(startVertex: Vector3d, endVertex: Vector3d, random: Random, jitterVertices: MutableList<Vector3d>, iterationsLeft: Int) {
        // Base case, nothing left to do
        if (iterationsLeft <= 0) {
            return
        }

        // Compute the "up" and "down" vectors relative to the current jitter's direction
        val forwardBackwardDirection = endVertex.subtract(startVertex).normalize()
        val (leftRightDirection, upDownDirection) = forwardBackwardDirection.getOrthogonalVectors()

        val distance = startVertex.distanceTo(endVertex)
        // Compute how 'far" the jitter is allowed to be away from the center of the chain
        val maxJitterOffset = distance * MAX_JITTER_OFFSET_RATIO
        // Take the midpoint of the start and end vertices
        val jitterVertex = startVertex.add(endVertex)
            .scale(0.5)
            // Add the random jitter
            .add(leftRightDirection.scale((random.nextDouble() - 0.5) * maxJitterOffset * 2))
            .add(upDownDirection.scale((random.nextDouble() - 0.5) * maxJitterOffset * 2))

        // Compute the left segment jitters, add the center jitter vertex, then the right segment jitters.
        // This ensures the jitter vertices are in the correct order.
        computeJitters(startVertex, jitterVertex, random, jitterVertices, iterationsLeft - 1)
        jitterVertices.add(jitterVertex)
        computeJitters(jitterVertex, endVertex, random, jitterVertices, iterationsLeft - 1)
    }

    private fun computeForks(random: Random, jitterVertices: List<Vector3d>): Map<Vector3d, List<Vector3d>> {
        if (jitterVertices.size <= 2) {
            return emptyMap()
        }

        val firstVertex = jitterVertices[0]
        val lastVertex = jitterVertices[jitterVertices.size - 1]
        val forwardBackwardDirection = lastVertex.subtract(firstVertex).normalize()
        val (leftRightDirection, upDownDirection) = forwardBackwardDirection.getOrthogonalVectors()
        val forks = mutableMapOf<Vector3d, List<Vector3d>>()
        // Can't fork the first or last vertex, so use 1 to size-1
        for (i in 1 until jitterVertices.size - 1) {
            val jitterVertex = jitterVertices[i]
            val maxForkDistance = jitterVertex.distanceTo(lastVertex)
            // 40% chance to make a fork at the jitter vertex
            if (random.nextDouble() < 0.4) {
                // Fork length takes between 70% and 100% of the max fork distance possible
                val forkDistance = (random.nextDouble() * 0.3 + 0.7) * maxForkDistance
                val numJittersInFork = forkDistance / APPROXIMATE_BLOCKS_PER_JITTER
                val numJitterSubdivisionsInFork = ceil(log(numJittersInFork, 2.0)).toInt()
                // Compute how 'far" the jitter is allowed to be away from the center of the chain
                val maxForkOffset = forkDistance * MAX_FORK_OFFSET_RATIO
                val forkEndVertex = jitterVertex.add(forwardBackwardDirection.scale(forkDistance))
                    // Add a random amount to the fork's end position
                    .add(leftRightDirection.scale((random.nextDouble() - 0.5) * maxForkOffset * 2))
                    .add(upDownDirection.scale((random.nextDouble() - 0.5) * maxForkOffset * 2))

                val forkVertices = mutableListOf<Vector3d>()
                computeJitters(jitterVertex, forkEndVertex, random, forkVertices, numJitterSubdivisionsInFork)
                forks[jitterVertex] = forkVertices
            }
        }

        return forks
    }

    private fun drawSegment(matrixStack: MatrixStack, buffer: IVertexBuilder, fromVertex: Vector3d, toVertex: Vector3d, width: Double) {
        val segmentLength = fromVertex.distanceTo(toVertex)

        val direction = toVertex.subtract(fromVertex).normalize()
        val rotation = BASE_RENDER_DIRECTION.computeRotationTo(direction)

        matrixStack.pushPose()
        matrixStack.mulPose(rotation)

        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()
        val rotationPerSprite = 180f / SPRITE_COUNT
        for (ignored in 0 until SPRITE_COUNT) {
            drawVertex(rotationMatrix, normalMatrix, buffer, 0.0, -width, 0.0, 0f, 0f)
            drawVertex(rotationMatrix, normalMatrix, buffer, segmentLength, -width, 0.0, 1f * segmentLength.toFloat(), 0f)
            drawVertex(rotationMatrix, normalMatrix, buffer, segmentLength, width, 0.0, 1f * segmentLength.toFloat(), 1f)
            drawVertex(rotationMatrix, normalMatrix, buffer, 0.0, width, 0.0, 0f, 1f)
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(rotationPerSprite))
        }

        matrixStack.popPose()

        val relativeSegmentEnd = direction.scale(segmentLength)
        matrixStack.translate(relativeSegmentEnd.x, relativeSegmentEnd.y, relativeSegmentEnd.z)
    }

    private fun drawVertex(
        rotationMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        vertexBuilder: IVertexBuilder,
        x: Double,
        y: Double,
        z: Double,
        u: Float,
        v: Float
    ) {
        vertexBuilder
            .vertex(rotationMatrix, x.toFloat(), y.toFloat(), z.toFloat())
            .color(255, 255, 0, 255)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(FULLBRIGHT)
            .normal(normalMatrix, 0f, 0f, 1f)
            .endVertex()
    }

    override fun getRenderOffset(entity: SpellChainEntity, partialTicks: Float): Vector3d {
        return Vector3d.ZERO
    }

    override fun getTextureLocation(entity: SpellChainEntity): ResourceLocation {
        return SPELL_CHAIN_TEXTURE
    }

    companion object {
        private const val FORK_WIDTH = 0.03
        private const val JITTER_WIDTH = 0.05
        private const val SPRITE_COUNT = 2
        private const val MAX_JITTER_OFFSET_RATIO = 1.0 / 10.0
        private const val MAX_FORK_OFFSET_RATIO = 1.0 / 3.0
        private const val APPROXIMATE_BLOCKS_PER_JITTER = 2

        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        private val BASE_RENDER_DIRECTION = Vector3d(1.0, 0.0, 0.0)

        // The texture used by the model
        private val SPELL_CHAIN_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/chain.png")

        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_chain",
            DefaultVertexFormats.NEW_ENTITY,
            GL11.GL_QUADS,
            256,
            false,
            true,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(SPELL_CHAIN_TEXTURE, false, false))
                .setTransparencyState(TransparencyState("no_transparency", { RenderSystem.disableBlend() }) {})
                .setCullState(RenderState.CullState(false))
                .setLightmapState(RenderState.LightmapState(false))
                .createCompositeState(true)
        )
    }
}
