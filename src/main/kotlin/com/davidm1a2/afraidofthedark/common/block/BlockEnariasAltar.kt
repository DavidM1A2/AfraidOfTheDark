package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.TileEntityEnariasAltar
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

/**
 * Enaria's altar block used in the nightmare to being crafting spells
 *
 * @constructor sets the block's properties
 */
class BlockEnariasAltar : AOTDBlockTileEntity(
    "enarias_altar",
    Properties.create(Material.PORTAL)
        .lightValue(1)
        .hardnessAndResistance(50.0f, 1200.0f)
) {
    override fun onBlockActivated(
        state: IBlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
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
                // playerIn.openGui(AOTDGuiHandler.SPELL_LIST_ID)
            } else {
                playerIn.sendMessage(TextComponentTranslation(LocalizationConstants.EnariasAltar.NO_RESEARCH))
            }
        }
        return true
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity {
        return TileEntityEnariasAltar()
    }
}