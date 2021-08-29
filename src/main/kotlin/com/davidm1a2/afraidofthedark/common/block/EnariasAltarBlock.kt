package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.gui.screens.SpellListScreen
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
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
    Properties.of(Material.PORTAL)
        .lightLevel { 1 }
        .strength(50.0f, 1200.0f)
) {
    override fun use(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType {
        // Grab the player's research
        val playerResearch = playerIn.getResearch()

        if (!worldIn.isClientSide) {
            if (!playerResearch.isResearched(ModResearches.ENARIAS_SECRET)) {
                playerIn.sendMessage(TranslationTextComponent("message.afraidofthedark.enarias_altar.no_research"), playerIn.uuid)
            }
        } else {
            // If the player has the right research show the gui
            if (playerResearch.isResearched(ModResearches.ENARIAS_SECRET) || playerResearch.canResearch(ModResearches.ENARIAS_SECRET)) {
                Minecraft.getInstance().setScreen(SpellListScreen())
            }
        }
        return ActionResultType.SUCCESS
    }

    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return EnariasAltarTileEntity()
    }

    override fun getShape(state: BlockState, world: IBlockReader, blockPos: BlockPos, context: ISelectionContext): VoxelShape {
        return ENARIAS_ALTAR_SHAPE
    }

    companion object {
        // For some reason, MC decides to render sides of blocks based on the shape. If the hitbox is not a full cube, we render sides. Make this "almost" a full cube
        private val ENARIAS_ALTAR_SHAPE = Block.box(0.001, 0.0, 0.001, 15.999, 15.999, 15.999)
    }
}