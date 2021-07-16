package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.block.ChestBlock
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Code is almost completely copied from ChestTileEntityRenderer
 */
@OnlyIn(Dist.CLIENT)
class TileEntityVoidChestRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) :
    TileEntityRenderer<VoidChestTileEntity>(tileEntityRendererDispatcher) {
    private val lid: ModelRenderer
    private val bottom: ModelRenderer
    private val latch: ModelRenderer

    init {
        bottom = ModelRenderer(64, 64, 0, 19)
        bottom.addBox(1.0f, 0.0f, 1.0f, 14.0f, 10.0f, 14.0f, 0.0f)
        lid = ModelRenderer(64, 64, 0, 0)
        lid.addBox(1.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f, 0.0f)
        lid.rotationPointY = 9.0f
        lid.rotationPointZ = 1.0f
        latch = ModelRenderer(64, 64, 0, 0)
        latch.addBox(7.0f, -1.0f, 15.0f, 2.0f, 4.0f, 1.0f, 0.0f)
        latch.rotationPointY = 8.0f
    }

    override fun render(
        voidChest: VoidChestTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderType: IRenderTypeBuffer,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        matrixStack.push()

        matrixStack.translate(0.5, 0.5, 0.5)
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-voidChest.blockState.get(ChestBlock.FACING).horizontalAngle))
        matrixStack.translate(-0.5, -0.5, -0.5)

        lid.rotateAngleX = -(MathHelper.lerp(partialTicks, voidChest.previousLidAngle, voidChest.lidAngle) * (Math.PI.toFloat() / 2f))
        latch.rotateAngleX = lid.rotateAngleX
        val buffer = renderType.getBuffer(RENDER_TYPE)

        lid.render(matrixStack, buffer, combinedLight, combinedOverlay)
        latch.render(matrixStack, buffer, combinedLight, combinedOverlay)
        bottom.render(matrixStack, buffer, combinedLight, combinedOverlay)

        matrixStack.pop()
    }


    companion object {
        private val VOID_CHEST_TEXTURE = ResourceLocation("afraidofthedark:textures/block/void_chest/void_chest.png")
        private val RENDER_TYPE = RenderType.getEntityCutout(VOID_CHEST_TEXTURE)
    }
}