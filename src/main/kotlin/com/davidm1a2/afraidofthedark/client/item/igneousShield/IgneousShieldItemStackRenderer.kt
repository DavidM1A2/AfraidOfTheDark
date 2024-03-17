package com.davidm1a2.afraidofthedark.client.item.igneousShield

import com.davidm1a2.afraidofthedark.common.constants.ModRenderMaterials
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.world.item.ItemStack

class IgneousShieldItemStackRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityWithoutLevelRenderer(context.blockEntityRenderDispatcher, context.modelSet) {
    override fun renderByItem(
        itemStack: ItemStack,
        ignored: ItemTransforms.TransformType,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
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