package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.FeyLightTileEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class FeyLightTileEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<FeyLightTileEntity> {
    override fun render(
        feyLight: FeyLightTileEntity,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderType: MultiBufferSource,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        // Nothing to render, tile entity renders using particles
    }
}