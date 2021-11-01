package com.davidm1a2.afraidofthedark.client.tileEntity.droppedJournal

import com.davidm1a2.afraidofthedark.common.tileEntity.DroppedJournalTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.LightType
import net.minecraft.world.World
import java.util.Random

class DroppedJournalTileEntityRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) : TileEntityRenderer<DroppedJournalTileEntity>(tileEntityRendererDispatcher) {
    private val random = SharedSeedRandom()

    override fun render(
        altarRuinsJournal: DroppedJournalTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderType: IRenderTypeBuffer,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        matrixStack.pushPose()

        matrixStack.translate(0.5, 0.03, 0.5)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.scale(.2f, .2f, .2f)

        random.setBaseChunkSeed(altarRuinsJournal.blockPos.x, altarRuinsJournal.blockPos.z)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat(0f, 360f)))

        val world = renderer.level
        val realLight = if (world != null) {
            LightTexture.pack(getLightAround(world, LightType.BLOCK, altarRuinsJournal.blockPos), getLightAround(world, LightType.SKY, altarRuinsJournal.blockPos))
        } else {
            combinedLight
        }
        DROPPED_JOURNAL_MODEL.renderToBuffer(
            matrixStack,
            renderType.getBuffer(RENDER_TYPE),
            realLight,
            combinedOverlay,
            1.0f,
            1.0f,
            1.0f,
            1.0f
        )
        matrixStack.popPose()
    }

    private fun getLightAround(world: World, lightType: LightType, pos: BlockPos): Int {
        val lightListener = world.lightEngine.getLayerListener(lightType)
        return maxOf(
            lightListener.getLightValue(pos.above()),
            lightListener.getLightValue(pos.north()),
            lightListener.getLightValue(pos.south()),
            lightListener.getLightValue(pos.east()),
            lightListener.getLightValue(pos.west())
        )
    }

    companion object {
        private val DROPPED_JOURNAL_MODEL = DroppedJournalTileEntityModel()
        private val DROPPED_JOURNAL_TEXTURE = ResourceLocation("afraidofthedark:textures/block/dropped_journal_te.png")
        private val RENDER_TYPE = DROPPED_JOURNAL_MODEL.renderType(DROPPED_JOURNAL_TEXTURE)

        private fun Random.nextFloat(min: Float, max: Float): Float {
            return min + nextFloat() * (max - min)
        }
    }
}