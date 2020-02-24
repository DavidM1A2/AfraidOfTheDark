package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.utility.openGui
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * Enaria's altar block used in the nightmare to being crafting spells
 *
 * @constructor sets the block's properties
 */
class BlockEnariasAltar : AOTDBlock("enarias_altar", Material.PORTAL) {
    init {
        setLightLevel(1.0f)
        setResistance(Float.MAX_VALUE)
        setBlockUnbreakable()
    }

    /**
     * Called when the block is right clicked
     *
     * @param worldIn  The world the block is in
     * @param pos      The position the block is at
     * @param state    The state of the block that is right clicked
     * @param playerIn The player that right clicked the block
     * @param hand     The hand that was used to right click
     * @param facing   The side of the block that was right clicked
     * @param hitX     The X position of the block that was right clicked
     * @param hitY     The Y position of the block that was right clicked
     * @param hitZ     The Z position of the block that was right clicked
     * @return True to cancel processing
     */
    override fun onBlockActivated(
        worldIn: World,
        pos: BlockPos,
        state: IBlockState,
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
                playerIn.openGui(AOTDGuiHandler.SPELL_LIST_ID)
            } else {
                playerIn.sendMessage(TextComponentTranslation("message.afraidofthedark:enarias_altar.no_research"))
            }
        }
        return true
    }

    /**
     * This block is not a full cube, it has a special model
     *
     * @param state The state of the block
     * @return False, this is not a full cube
     */
    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    /**
     * False, this block lets light through
     *
     * @param state The block state to test
     * @return False since the block lets light through
     */
    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }
}