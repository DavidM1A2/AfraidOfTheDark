package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.gui.screens.SpellListScreen
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

/**
 * Enaria's altar block used in the nightmare to being crafting spells
 *
 * @constructor sets the block's properties
 */
class EnariasAltarBlock : AOTDTileEntityBlock(
    "enarias_altar",
    Properties.create(Material.PORTAL)
        .lightValue(1)
        .hardnessAndResistance(50.0f, 1200.0f)
) {
    override fun onBlockActivated(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        hit: BlockRayTraceResult
    ): Boolean {
        // Grab the player's research
        val playerResearch = playerIn.getResearch()

        // Server side processing research
        if (!worldIn.isRemote) {
            // If the player can research enaria's secret do so
            if (playerResearch.canResearch(ModResearches.ENARIAS_SECRET)) {
                playerResearch.setResearch(ModResearches.ENARIAS_SECRET, true)
                playerResearch.sync(playerIn, true)
            }
        } else {
            // If the player has the right research show the gui
            if (playerResearch.isResearched(ModResearches.ENARIAS_SECRET)) {
                Minecraft.getInstance().displayGuiScreen(SpellListScreen())
            } else {
                playerIn.sendMessage(TranslationTextComponent("message.afraidofthedark.enarias_altar.no_research"))
            }
        }
        return true
    }

    override fun getShape(state: BlockState, world: IBlockReader, blockPos: BlockPos, selectionContext: ISelectionContext): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity {
        return EnariasAltarTileEntity()
    }
}