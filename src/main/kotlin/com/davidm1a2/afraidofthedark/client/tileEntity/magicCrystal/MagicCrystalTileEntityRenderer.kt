package com.davidm1a2.afraidofthedark.client.tileEntity.magicCrystal

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f

class MagicCrystalTileEntityRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) : TileEntityRenderer<MagicCrystalTileEntity>(tileEntityRendererDispatcher) {
    override fun render(
        magicCrystal: MagicCrystalTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderType: IRenderTypeBuffer,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        // Only render the bottom one
        if (!ModBlocks.MAGIC_CRYSTAL.isBottom(magicCrystal.blockState)) {
            return
        }

        matrixStack.pushPose()
        matrixStack.translate(0.5, magicCrystal.getRenderHeightOffset(partialTicks), 0.5)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(magicCrystal.getRenderRotation(partialTicks).toFloat()))

        MAGIC_CRYSTAL_MODEL.renderToBuffer(
            matrixStack,
            renderType.getBuffer(RENDER_TYPE),
            combinedLight,
            combinedOverlay,
            1.0f,
            1.0f,
            1.0f,
            1.0f
        )
        matrixStack.popPose()
    }

    companion object {
        private val MAGIC_CRYSTAL_MODEL = MagicCrystalTileEntityModel()
        private val MAGIC_CRYSTAL_TEXTURE = ResourceLocation("afraidofthedark:textures/block/magic_crystal_te.png")
        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "magic_crystal",
            DefaultVertexFormats.NEW_ENTITY,
            7,
            256,
            false,
            false,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(MAGIC_CRYSTAL_TEXTURE, false, false))
                .setTransparencyState(RenderState.TransparencyState("no_transparency", { RenderSystem.disableBlend() }) {})
                .setDiffuseLightingState(RenderState.DiffuseLightingState(true))
                .setAlphaState(RenderState.AlphaState(0.003921569F))
                .setLightmapState(RenderState.LightmapState(true))
                .setOverlayState(RenderState.OverlayState(true))
                .createCompositeState(true)
        )
    }
}