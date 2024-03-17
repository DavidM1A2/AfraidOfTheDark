package com.davidm1a2.afraidofthedark.client.tileEntity.magicCrystal

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation

class MagicCrystalTileEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<MagicCrystalTileEntity> {
    override fun render(
        magicCrystal: MagicCrystalTileEntity,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderType: MultiBufferSource,
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
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                .setTextureState(RenderStateShard.TextureStateShard(MAGIC_CRYSTAL_TEXTURE, false, false))
                .setTransparencyState(RenderStateShard.TransparencyStateShard("no_transparency", { RenderSystem.disableBlend() }) {})
                .setShaderState(RenderStateShard.ShaderStateShard(GameRenderer::getNewEntityShader))
                .setLightmapState(RenderStateShard.LightmapStateShard(true))
                .setOverlayState(RenderStateShard.OverlayStateShard(true))
                .createCompositeState(true)
        )
    }
}