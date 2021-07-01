package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import kotlin.random.Random

/**
 * Block that coverts glass into lens items
 */
class LensCutterBlock : AOTDBlock(
    "lens_cutter",
    Properties.create(Material.IRON)
        .hardnessAndResistance(5.0F, 6.0F)
        .sound(SoundType.METAL)
) {
    override fun onBlockActivated(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        result: BlockRayTraceResult
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
                if (heldItem.item == Blocks.GLASS.asItem()) {
                    heldItem.shrink(1)
                    worldIn.playSound(null, pos, ModSounds.LENS_CUTTER, SoundCategory.BLOCKS, 0.5f, Random.nextDouble(0.8, 1.2).toFloat())
                    playerIn.addItemStackToInventory(ItemStack(ModItems.LENS))
                } else {
                    playerIn.sendMessage(TranslationTextComponent("message.afraidofthedark.lens_cutter.wrong_item"))
                }
            } else {
                playerIn.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }

        return true
    }
}