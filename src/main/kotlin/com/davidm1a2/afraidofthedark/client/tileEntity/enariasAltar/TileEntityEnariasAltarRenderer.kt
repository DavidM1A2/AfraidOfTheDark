package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TileEntityEnariasAltarRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) :
    TileEntityRenderer<EnariasAltarTileEntity>(tileEntityRendererDispatcher) {
    private val model = TileEntityEnariasAltarModel()
    private val texture = ResourceLocation("afraidofthedark:textures/block/enarias_altar_te.png")

    override fun render(
        te: EnariasAltarTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int,
        packedOverlay: Int
    ) {
        model.performAnimations(te)
        te.getAnimationHandler().update()

        val renderBuffer = renderTypeBuffer.getBuffer(model.getRenderType(texture))
        model.render(matrixStack, renderBuffer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
    }
}