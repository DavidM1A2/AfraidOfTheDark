package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.davidm1a2.afraidofthedark.common.block.VitaeExtractorBlock
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.item.VitaeLanternItem
import com.davidm1a2.afraidofthedark.common.tileEntity.VitaeExtractorTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.LightType
import net.minecraft.world.World

class VitaeExtractorTileEntityRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) :
    TileEntityRenderer<VitaeExtractorTileEntity>(tileEntityRendererDispatcher) {

    override fun render(
        te: VitaeExtractorTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int,
        packedOverlay: Int
    ) {
        matrixStack.pushPose()
        matrixStack.translate(0.5, 0.0, 0.5)
        val facing = te.blockState.getValue(VitaeExtractorBlock.FACING)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-(facing.toYRot() + 90) % 360))

        val world = renderer.level
        val realLight = if (world != null) {
            LightTexture.pack(getLightAround(world, LightType.BLOCK, te.blockPos), getLightAround(world, LightType.SKY, te.blockPos))
        } else {
            packedLight
        }
        if (te.isBurningFuel()) {
            VITAE_EXTRACTOR_MODEL.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(EXTRACTOR_ACTIVE_RENDER_TYPE), realLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
        } else {
            VITAE_EXTRACTOR_MODEL.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(EXTRACTOR_RENDER_TYPE), realLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
        }

        val lantern = te.getLantern()
        if (!lantern.isEmpty) {
            matrixStack.translate(0.0, 0.48, 0.0)
            val changeLevel = ModItems.VITAE_LANTERN.getChargeLevel(lantern)
            VITAE_LANTERN_MODEL.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(CHARGE_LEVEL_TO_RENDER_TYPE[changeLevel]!!), realLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
        }

        matrixStack.popPose()
    }

    private fun getLightAround(world: World, lightType: LightType, pos: BlockPos): Int {
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
        private val VITAE_EXTRACTOR_MODEL = VitaeExtractorTileEntityModel()
        private val VITAE_EXTRACTOR_TEXTURE = ResourceLocation("afraidofthedark:textures/block/vitae_extractor_te.png")
        private val VITAE_EXTRACTOR_ACTIVE_TEXTURE = ResourceLocation("afraidofthedark:textures/block/vitae_extractor_active_te.png")
        private val EXTRACTOR_RENDER_TYPE = VITAE_EXTRACTOR_MODEL.renderType(VITAE_EXTRACTOR_TEXTURE)
        private val EXTRACTOR_ACTIVE_RENDER_TYPE = VITAE_EXTRACTOR_MODEL.renderType(VITAE_EXTRACTOR_ACTIVE_TEXTURE)

        private val VITAE_LANTERN_MODEL = VitaeLanternModel()
        private val CHARGE_LEVEL_TO_RENDER_TYPE = mapOf(
            VitaeLanternItem.ChargeLevel.EMPTY to VITAE_LANTERN_MODEL.renderType(ResourceLocation(Constants.MOD_ID, "textures/block/vitae_lantern/vitae_lantern_empty.png")),
            VitaeLanternItem.ChargeLevel.QUARTER to VITAE_LANTERN_MODEL.renderType(ResourceLocation(Constants.MOD_ID, "textures/block/vitae_lantern/vitae_lantern_quarter.png")),
            VitaeLanternItem.ChargeLevel.HALF to VITAE_LANTERN_MODEL.renderType(ResourceLocation(Constants.MOD_ID, "textures/block/vitae_lantern/vitae_lantern_half.png")),
            VitaeLanternItem.ChargeLevel.THREE_QUARTERS to VITAE_LANTERN_MODEL.renderType(ResourceLocation(Constants.MOD_ID, "textures/block/vitae_lantern/vitae_lantern_three_quarters.png")),
            VitaeLanternItem.ChargeLevel.FULL to VITAE_LANTERN_MODEL.renderType(ResourceLocation(Constants.MOD_ID, "textures/block/vitae_lantern/vitae_lantern_full.png"))
        )
    }
}