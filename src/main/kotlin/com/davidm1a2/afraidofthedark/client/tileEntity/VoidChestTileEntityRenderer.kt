package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.level.block.ChestBlock

/**
 * Code is almost completely copied from ChestTileEntityRenderer
 */
class VoidChestTileEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<VoidChestTileEntity> {
    private val lid: ModelPart
    private val bottom: ModelPart
    private val latch: ModelPart

    init {
        bottom = ModelPart(listOf(makeDefaultCube(0, 19, 1.0f, 0.0f, 1.0f, 14.0f, 10.0f, 14.0f)), emptyMap())
        lid = ModelPart(listOf(makeDefaultCube(0, 0, 1.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f)), emptyMap())
        lid.y = 9.0f
        lid.z = 1.0f
        latch = ModelPart(listOf(makeDefaultCube(0, 0, 7.0f, -1.0f, 15.0f, 2.0f, 4.0f, 1.0f)), emptyMap())
        latch.y = 8.0f
    }

    override fun render(
        voidChest: VoidChestTileEntity,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderType: MultiBufferSource,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        matrixStack.pushPose()

        matrixStack.translate(0.5, 0.5, 0.5)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-voidChest.blockState.getValue(ChestBlock.FACING).toYRot()))
        matrixStack.translate(-0.5, -0.5, -0.5)

        lid.xRot = -(Mth.lerp(partialTicks, voidChest.previousLidAngle, voidChest.lidAngle) * (Math.PI.toFloat() / 2f))
        latch.xRot = lid.xRot
        val buffer = renderType.getBuffer(RENDER_TYPE)

        lid.render(matrixStack, buffer, combinedLight, combinedOverlay)
        latch.render(matrixStack, buffer, combinedLight, combinedOverlay)
        bottom.render(matrixStack, buffer, combinedLight, combinedOverlay)

        matrixStack.popPose()
    }

    private fun makeDefaultCube(texOffsX: Int, texOffsY: Int, x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float): ModelPart.Cube {
        return ModelPart.Cube(texOffsX, texOffsY, x, y, z, width, height, depth, 0f, 0f, 0f, false, 64f, 64f)
    }

    companion object {
        private val VOID_CHEST_TEXTURE = ResourceLocation("afraidofthedark:textures/block/void_chest/void_chest.png")
        private val RENDER_TYPE = RenderType.entityCutout(VOID_CHEST_TEXTURE)
    }
}