package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntitySpellAltar
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class used to render the void chest. Most of this code is from 'TileEntityEnderChestRenderer'
 */
@OnlyIn(Dist.CLIENT)
class TileEntitySpellAltarRenderer : TileEntityRenderer<TileEntitySpellAltar>() {
    /**
     * Called to render the tile entity
     *
     * @param te           The tile entity to render
     * @param x            The x position of the tile entity
     * @param y            The y position of the tile entity
     * @param z            The z position of the tile entity
     * @param partialTicks How much time has passed since the last tick
     * @param destroyStage How far the block is destroyed
     * @param alpha        The alpha value to render with
     */
    override fun render(
        te: TileEntitySpellAltar,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int
    ) {
        /*
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 1.0, z + 0.5)
        bindTexture(TEXTURE)
        MODEL.render(te, 0.0625f)
        GlStateManager.popMatrix()
         */
    }
}