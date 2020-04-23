package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import net.minecraft.client.model.ModelChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class used to render the void chest. Most of this code is from 'TileEntityEnderChestRenderer'
 *
 * @property voidChestTexture The texture for the void chest
 * @property modelChest Create a chest model that we render our void chest with
 */
@SideOnly(Side.CLIENT)
class TileEntityVoidChestRenderer : TileEntitySpecialRenderer<TileEntityVoidChest>() {
    private val voidChestTexture = ResourceLocation("afraidofthedark:textures/blocks/void_chest/void_chest.png")
    private val modelChest = ModelChest()

    /**
     * Called to render the tile entity
     *
     * @param te           The tile entity to render
     * @param x            The x position of the tile entity
     * @param y            The y position of the tile entity
     * @param z            The z position of the tile entity
     * @param partialTicks How much time has passed since the last tick
     * @param destroyStage How far the chest is destroyed
     * @param alpha        The alpha value to render with
     */
    override fun render(
        te: TileEntityVoidChest,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int,
        alpha: Float
    ) {
        ///
        /// All code below is from TileEntityEnderChestRender.class::render()
        ///

        var i = 0
        if (te.hasWorld()) {
            i = te.blockMetadata
        }
        if (destroyStage >= 0) {
            bindTexture(DESTROY_STAGES[destroyStage])
            GlStateManager.matrixMode(5890)
            GlStateManager.pushMatrix()
            GlStateManager.scale(4.0f, 4.0f, 1.0f)
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f)
            GlStateManager.matrixMode(5888)
        } else {
            bindTexture(voidChestTexture)
        }
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha)
        GlStateManager.translate(x.toFloat(), y.toFloat() + 1.0f, z.toFloat() + 1.0f)
        GlStateManager.scale(1.0f, -1.0f, -1.0f)
        GlStateManager.translate(0.5f, 0.5f, 0.5f)
        var j = 0
        if (i == 2) {
            j = 180
        }
        if (i == 3) {
            j = 0
        }
        if (i == 4) {
            j = 90
        }
        if (i == 5) {
            j = -90
        }
        GlStateManager.rotate(j.toFloat(), 0.0f, 1.0f, 0.0f)
        GlStateManager.translate(-0.5f, -0.5f, -0.5f)
        var f = te.previousLidAngle + (te.lidAngle - te.previousLidAngle) * partialTicks
        f = 1.0f - f
        f = 1.0f - f * f * f
        modelChest.chestLid.rotateAngleX = -(f * (Math.PI.toFloat() / 2f))
        modelChest.renderAll()
        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890)
            GlStateManager.popMatrix()
            GlStateManager.matrixMode(5888)
        }
    }
}