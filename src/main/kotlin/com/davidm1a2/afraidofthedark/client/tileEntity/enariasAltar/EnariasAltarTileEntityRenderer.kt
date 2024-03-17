package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer

class EnariasAltarTileEntityRenderer(private val context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<EnariasAltarTileEntity> {

    override fun render(
        te: EnariasAltarTileEntity,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        ENARIAS_ALTAR_MODEL.performAnimations(te)
        te.getAnimationHandler().update()

        matrixStack.pushPose()
        matrixStack.translate(0.5, 0.0, 0.5)
        val world = context.blockEntityRenderDispatcher.level
        val realLight = if (world != null) {
            LightTexture.pack(getLightAround(world, LightLayer.BLOCK, te.blockPos), getLightAround(world, LightLayer.SKY, te.blockPos))
        } else {
            packedLight
        }
        ENARIAS_ALTAR_MODEL.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RENDER_TYPE), realLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
        matrixStack.popPose()
    }

    private fun getLightAround(world: Level, lightType: LightLayer, pos: BlockPos): Int {
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
        private val ENARIAS_ALTAR_MODEL = EnariasAltarTileEntityModel()
        private val ENARIAS_ALTAR_TEXTURE = ResourceLocation("afraidofthedark:textures/block/enarias_altar_te.png")
        private val RENDER_TYPE = ENARIAS_ALTAR_MODEL.renderType(ENARIAS_ALTAR_TEXTURE)
    }
}