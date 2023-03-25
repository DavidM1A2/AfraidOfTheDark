package com.davidm1a2.afraidofthedark.client.item.igneousShield

import com.davidm1a2.afraidofthedark.common.constants.ModRenderMaterials
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.item.ItemStack

class IgneousShieldItemStackRenderer : ItemStackTileEntityRenderer() {
    override fun renderByItem(
        itemStack: ItemStack,
        ignored: ItemCameraTransforms.TransformType,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int,
        packedOverlay: Int
    ) {
        matrixStack.pushPose()
        matrixStack.scale(1.0f, -1.0f, -1.0f)
        val iVertexBuilder = ModRenderMaterials.IGNEOUS_SHIELD.sprite().wrap(
            ItemRenderer.getFoilBufferDirect(renderTypeBuffer, SHIELD_MODEL.renderType(ModRenderMaterials.IGNEOUS_SHIELD.atlasLocation()), true, itemStack.hasFoil())
        )
        SHIELD_MODEL.renderToBuffer(matrixStack, iVertexBuilder, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)

        matrixStack.popPose()
    }

    companion object {
        private val SHIELD_MODEL = IgneousShieldModel()
    }
}