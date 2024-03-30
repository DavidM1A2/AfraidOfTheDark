package com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class SpellCraftingTableItemStackRenderer : BlockEntityWithoutLevelRenderer(Minecraft.getInstance().blockEntityRenderDispatcher, Minecraft.getInstance().entityModels) {
    override fun renderByItem(
        itemStack: ItemStack,
        ignored: ItemTransforms.TransformType,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
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