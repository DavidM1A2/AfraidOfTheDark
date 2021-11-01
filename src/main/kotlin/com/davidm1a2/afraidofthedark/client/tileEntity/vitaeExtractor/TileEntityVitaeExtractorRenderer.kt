package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.davidm1a2.afraidofthedark.common.block.VitaeExtractorBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.VitaeExtractorTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.LightType
import net.minecraft.world.World

class TileEntityVitaeExtractorRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) :
    TileEntityRenderer<VitaeExtractorTileEntity>(tileEntityRendererDispatcher) {

    override fun render(
        te: VitaeExtractorTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int,
        packedOverlay: Int
    ) {
        matrixStack.pushPose()
        matrixStack.translate(0.5, 0.0, 0.5)
        val facing = te.blockState.getValue(VitaeExtractorBlock.FACING)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-(facing.toYRot() + 90) % 360))

        val world = renderer.level
        val realLight = if (world != null) {
            LightTexture.pack(getLightAround(world, LightType.BLOCK, te.blockPos), getLightAround(world, LightType.SKY, te.blockPos))
        } else {
            packedLight
        }
        VITAE_EXTRACTOR_MODEL.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RENDER_TYPE), realLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
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
        private val VITAE_EXTRACTOR_MODEL = TileEntityVitaeExtractorModel()
        private val VITAE_EXTRACTOR_TEXTURE = ResourceLocation("afraidofthedark:textures/block/vitae_extractor_te.png")
        private val RENDER_TYPE = VITAE_EXTRACTOR_MODEL.renderType(VITAE_EXTRACTOR_TEXTURE)
    }
}