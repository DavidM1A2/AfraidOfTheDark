package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class used to render the enaria's altar
 *
 * @property model The altar's model
 * @property texture The altar's texture
 */
@OnlyIn(Dist.CLIENT)
class TileEntityEnariasAltarRenderer : TileEntityRenderer<EnariasAltarTileEntity>() {
    private val model = TileEntityEnariasAltarModel()
    private val texture = ResourceLocation("afraidofthedark:textures/block/enarias_altar_te.png")

    /**
     * Called to render the tile entity
     *
     * @param te           The tile entity to render
     * @param x            The x position of the tile entity
     * @param y            The y position of the tile entity
     * @param z            The z position of the tile entity
     * @param partialTicks How much time has passed since the last tick
     * @param destroyStage How far the block is destroyed
     */
    override fun render(
        te: EnariasAltarTileEntity,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int
    ) {
        GlStateManager.pushMatrix()
        GlStateManager.translatef(x.toFloat() + 0.5f, y.toFloat(), z.toFloat() + 0.5f)
        bindTexture(texture)
        te.getAnimationHandler().update()
        model.render(te, 0.0625f)
        GlStateManager.popMatrix()
    }
}