package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f

class VitaeExtractorItemStackRenderer : ItemStackTileEntityRenderer() {
    override fun renderByItem(itemStack: ItemStack, ignored: ItemCameraTransforms.TransformType, matrixStack: MatrixStack, renderTypeBuffer: IRenderTypeBuffer, packedLight: Int, packedOverlay: Int) {
        matrixStack.pushPose()
        matrixStack.translate(0.5, 0.0, 0.5)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-(Direction.NORTH.toYRot() + 90) % 360))

        VITAE_EXTRACTOR_MODEL.renderToBuffer(
            matrixStack,
            renderTypeBuffer.getBuffer(RENDER_TYPE),
            packedLight,
            packedOverlay,
            1.0f,
            1.0f,
            1.0f,
            1.0f
        )
        matrixStack.popPose()
    }

    companion object {
        private val VITAE_EXTRACTOR_MODEL = VitaeExtractorTileEntityModel()
        private val VITAE_EXTRACTOR_TEXTURE = ResourceLocation("afraidofthedark:textures/block/vitae_extractor_te.png")
        private val RENDER_TYPE = VITAE_EXTRACTOR_MODEL.renderType(VITAE_EXTRACTOR_TEXTURE)
    }
}