package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.LightType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TileEntityEnariasAltarRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) :
    TileEntityRenderer<EnariasAltarTileEntity>(tileEntityRendererDispatcher) {

    override fun render(
        te: EnariasAltarTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int,
        packedOverlay: Int
    ) {
        ENARIAS_ALTAR_MODEL.performAnimations(te)
        te.getAnimationHandler().update()

        matrixStack.push()
        matrixStack.translate(0.5, 0.0, 0.5)
        val world = renderDispatcher.world
        val realLight = if (world != null) {
            LightTexture.packLight(getLightAround(world, LightType.BLOCK, te.pos), getLightAround(world, LightType.SKY, te.pos))
        } else {
            packedLight
        }
        ENARIAS_ALTAR_MODEL.render(matrixStack, renderTypeBuffer.getBuffer(RENDER_TYPE), realLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
        matrixStack.pop()
    }

    private fun getLightAround(world: World, lightType: LightType, pos: BlockPos): Int {
        return maxOf(
            world.getLightFor(lightType, pos.up()),
            world.getLightFor(lightType, pos.north()),
            world.getLightFor(lightType, pos.south()),
            world.getLightFor(lightType, pos.east()),
            world.getLightFor(lightType, pos.west())
        )
    }

    companion object {
        private val ENARIAS_ALTAR_MODEL = TileEntityEnariasAltarModel()
        private val ENARIAS_ALTAR_TEXTURE = ResourceLocation("afraidofthedark:textures/block/enarias_altar_te.png")
        private val RENDER_TYPE = ENARIAS_ALTAR_MODEL.getRenderType(ENARIAS_ALTAR_TEXTURE)
    }
}