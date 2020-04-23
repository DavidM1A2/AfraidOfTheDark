package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.TileEntityEnariasAltar
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class used to render the enaria's altar
 *
 * @property model The altar's model
 * @property texture The altar's texture
 */
@SideOnly(Side.CLIENT)
class TileEntityEnariasAltarRenderer : TileEntitySpecialRenderer<TileEntityEnariasAltar>() {
    private val model = TileEntityEnariasAltarModel()
    private val texture = ResourceLocation("afraidofthedark:textures/blocks/enarias_altar_te.png")

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
        te: TileEntityEnariasAltar,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int,
        alpha: Float
    ) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y, z + 0.5)
        bindTexture(texture)
        model.render(te, 0.0625f)
        GlStateManager.popMatrix()
    }
}