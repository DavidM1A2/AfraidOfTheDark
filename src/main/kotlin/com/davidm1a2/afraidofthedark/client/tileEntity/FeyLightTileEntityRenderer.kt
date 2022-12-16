package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.FeyLightTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher

class FeyLightTileEntityRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) : TileEntityRenderer<FeyLightTileEntity>(tileEntityRendererDispatcher) {
    override fun render(
        feyLight: FeyLightTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderType: IRenderTypeBuffer,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        // Nothing to render, tile entity renders using particles
    }
}