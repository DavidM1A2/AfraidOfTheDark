package com.davidm1a2.afraidofthedark.client.tileEntity.droppedJournal

import com.davidm1a2.afraidofthedark.common.tileEntity.DroppedJournalTileEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.levelgen.WorldgenRandom

class DroppedJournalTileEntityRenderer(private val context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<DroppedJournalTileEntity> {
    private val random = WorldgenRandom()

    override fun render(
        altarRuinsJournal: DroppedJournalTileEntity,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderType: MultiBufferSource,
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

        val world = context.blockEntityRenderDispatcher.level
        val realLight = if (world != null) {
            LightTexture.pack(getLightAround(world, LightLayer.BLOCK, altarRuinsJournal.blockPos), getLightAround(world, LightLayer.SKY, altarRuinsJournal.blockPos))
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

    private fun getLightAround(world: Level, lightType: LightLayer, pos: BlockPos): Int {
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
    }
}