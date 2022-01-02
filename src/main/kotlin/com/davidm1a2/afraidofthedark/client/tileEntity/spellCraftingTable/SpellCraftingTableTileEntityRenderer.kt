package com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable

import com.davidm1a2.afraidofthedark.common.block.SpellCraftingTableBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.SpellCraftingTableTileEntity
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

class SpellCraftingTableTileEntityRenderer(tileEntityRendererDispatcher: TileEntityRendererDispatcher) : TileEntityRenderer<SpellCraftingTableTileEntity>(tileEntityRendererDispatcher) {
    override fun render(
        spellCraftingTable: SpellCraftingTableTileEntity,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        combinedLight: Int,
        combinedOverlay: Int
    ) {
        matrixStack.pushPose()
        matrixStack.translate(0.5, 1.0, 0.5)
        matrixStack.scale(0.5f, 0.5f, 0.5f)
        val facing = spellCraftingTable.blockState.getValue(SpellCraftingTableBlock.FACING)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-(facing.toYRot() + 90) % 360))

        val world = renderer.level
        val realLight = if (world != null) {
            LightTexture.pack(getLightAround(world, LightType.BLOCK, spellCraftingTable.blockPos), getLightAround(world, LightType.SKY, spellCraftingTable.blockPos))
        } else {
            combinedLight
        }
        SPELL_CRAFTING_TABLE_MODEL.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RENDER_TYPE), realLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
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
        private val SPELL_CRAFTING_TABLE_MODEL = SpellCraftingTableModel()
        private val SPELL_CRAFTING_TABLE_TEXTURE = ResourceLocation("afraidofthedark:textures/block/spell_crafting_table_te.png")
        private val RENDER_TYPE = SPELL_CRAFTING_TABLE_MODEL.renderType(SPELL_CRAFTING_TABLE_TEXTURE)
    }
}