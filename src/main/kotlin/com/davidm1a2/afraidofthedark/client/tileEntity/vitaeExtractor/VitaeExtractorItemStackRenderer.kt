package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class VitaeExtractorItemStackRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityWithoutLevelRenderer(context.blockEntityRenderDispatcher, context.modelSet) {
    override fun renderByItem(
        itemStack: ItemStack,
        ignored: ItemTransforms.TransformType,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
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