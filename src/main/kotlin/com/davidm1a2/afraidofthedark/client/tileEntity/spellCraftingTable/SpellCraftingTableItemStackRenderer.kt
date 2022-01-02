package com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f

class SpellCraftingTableItemStackRenderer : ItemStackTileEntityRenderer() {
    override fun renderByItem(
        itemStack: ItemStack,
        ignored: ItemCameraTransforms.TransformType,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int,
        packedOverlay: Int
    ) {
        matrixStack.pushPose()
        matrixStack.translate(0.5, 1.0, 0.5)
        matrixStack.scale(0.5f, 0.5f, 0.5f)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-(Direction.NORTH.toYRot() + 270) % 360))

        SPELL_CRAFTING_TABLE_MODEL.renderToBuffer(
            matrixStack,
            renderTypeBuffer.getBuffer(RENDER_TYPE),
            packedLight,
            packedOverlay,
            1.0f,
            1.0f,
            1.0f,
            1.0f
        )
        matrixStack.popPose()
    }

    companion object {
        private val SPELL_CRAFTING_TABLE_MODEL = SpellCraftingTableModel()
        private val SPELL_CRAFTING_TABLE_TEXTURE = ResourceLocation("afraidofthedark:textures/block/spell_crafting_table_te.png")
        private val RENDER_TYPE = SPELL_CRAFTING_TABLE_MODEL.renderType(SPELL_CRAFTING_TABLE_TEXTURE)
    }
}