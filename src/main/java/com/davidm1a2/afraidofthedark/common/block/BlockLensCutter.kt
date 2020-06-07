package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import kotlin.random.Random

/**
 * Block that coverts glass into lens items
 */
class BlockLensCutter : AOTDBlock("lens_cutter", Material.IRON) {
    /**
     * Called when the block is right clicked by a player
     *
     * @param worldIn The world the block is in
     * @param pos The position the block is at
     * @param state The state of the block
     * @param playerIn The player that right clicked the block
     * @param hand The hand that the item the player clicked with is in
     * @param facing The side of the block that was clicked
     * @param hitX The x pixel hit by the player
     * @param hitY The y pixel hit by the player
     * @param hitZ The z pixel hit by the player
     * @return True to indicate a successful block interaction, false otherwise
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
        // Server side processing only
        if (!worldIn.isRemote) {
            // Unlock optics if possible
            val research = playerIn.getResearch()
            if (research.canResearch(ModResearches.OPTICS)) {
                research.setResearch(ModResearches.OPTICS, true)
                research.sync(playerIn, true)
            }

            // Check if the player has the research to use the block
            if (research.isResearched(ModResearches.OPTICS)) {
                val heldItem = playerIn.getHeldItem(hand)
                // If they're holding glass reduce the stack size by one and add a lens item
                if (heldItem.item == Item.getItemFromBlock(Blocks.GLASS)) {
                    heldItem.shrink(1)
                    worldIn.playSound(null, pos, ModSounds.LENS_CUTTER, SoundCategory.BLOCKS, 0.5f, Random.nextDouble(0.8, 1.2).toFloat())
                    playerIn.addItemStackToInventory(ItemStack(ModItems.LENS))
                } else {
                    playerIn.sendMessage(TextComponentTranslation(LocalizationConstants.Block.LENS_CUTTER_WRONG_ITEM))
                }
            } else {
                playerIn.sendMessage(TextComponentTranslation(LocalizationConstants.Generic.DONT_UNDERSTAND))
            }
        }

        return true
    }
}