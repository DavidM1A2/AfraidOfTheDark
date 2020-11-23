package com.davidm1a2.afraidofthedark.client.tileEntity

import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ChestBlock
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.model.ChestModel
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import kotlin.math.abs

/**
 * Class used to render the void chest. Most of this code is from 'TileEntityEnderChestRenderer'
 *
 * @property voidChestTexture The texture for the void chest
 * @property modelChest Create a chest model that we render our void chest with
 */
@OnlyIn(Dist.CLIENT)
class TileEntityVoidChestRenderer : TileEntityRenderer<VoidChestTileEntity>() {
    private val voidChestTexture = ResourceLocation("afraidofthedark:textures/block/void_chest/void_chest.png")
    private val modelChest = ChestModel()

    /**
     * Called to render the tile entity
     *
     * @param te           The tile entity to render
     * @param x            The x position of the tile entity
     * @param y            The y position of the tile entity
     * @param z            The z position of the tile entity
     * @param partialTicks How much time has passed since the last tick
     * @param destroyStage How far the chest is destroyed
     */
    override fun render(
        te: VoidChestTileEntity,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int
    ) {
        ///
        /// All code below is from TileEntityEnderChestRender.class::render()
        ///

        GlStateManager.enableDepthTest()
        GlStateManager.depthFunc(515)
        GlStateManager.depthMask(true)
        val blockState = (if (te.hasWorld()) te.blockState else Blocks.CHEST.defaultState.with(ChestBlock.FACING, Direction.SOUTH) as BlockState)!!
        bindTexture(voidChestTexture)
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890)
            GlStateManager.pushMatrix()
            GlStateManager.scalef(4.0f, 4.0f, 1.0f)
            GlStateManager.translatef(0.0625f, 0.0625f, 0.0625f)
            GlStateManager.matrixMode(5888)
        } else {
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        }
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.translatef(x.toFloat(), y.toFloat() + 1.0f, z.toFloat() + 1.0f)
        GlStateManager.scalef(1.0f, -1.0f, -1.0f)
        val angle = (blockState.get(ChestBlock.FACING) as Direction).horizontalAngle
        if (abs(angle).toDouble() > 1.0E-5) {
            GlStateManager.translatef(0.5f, 0.5f, 0.5f)
            GlStateManager.rotatef(angle, 0.0f, 1.0f, 0.0f)
            GlStateManager.translatef(-0.5f, -0.5f, -0.5f)
        }
        var f = te.previousLidAngle + (te.lidAngle - te.previousLidAngle) * partialTicks
        f = 1.0f - f
        f = 1.0f - f * f * f
        modelChest.lid.rotateAngleX = -(f * (Math.PI.toFloat() / 2f))
        modelChest.renderAll()
        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890)
            GlStateManager.popMatrix()
            GlStateManager.matrixMode(5888)
        }
    }
}