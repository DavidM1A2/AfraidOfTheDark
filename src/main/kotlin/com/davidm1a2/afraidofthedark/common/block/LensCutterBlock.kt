package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult
import kotlin.random.Random

/**
 * Block that coverts glass into lens items
 */
class LensCutterBlock : AOTDBlock(
    "lens_cutter",
    Properties.of(Material.METAL)
        .strength(5.0F, 6.0F)
        .sound(SoundType.METAL)
) {
    override fun use(
        state: BlockState,
        worldIn: Level,
        pos: BlockPos,
        playerIn: Player,
        hand: InteractionHand,
        result: BlockHitResult
    ): InteractionResult {
        // Server side processing only
        if (!worldIn.isClientSide) {
            val research = playerIn.getResearch()

            // Check if the player has the research to use the block
            if (research.isResearched(ModResearches.OPTICS)) {
                val heldItemStack = playerIn.getItemInHand(hand)
                val heldItem = heldItemStack.item
                // If they're holding glass reduce the stack size by one and add a lens item
                if (RECIPES.containsKey(heldItem)) {
                    if (!playerIn.isCreative) {
                        heldItemStack.shrink(1)
                    }
                    worldIn.playSound(null, pos, ModSounds.LENS_CUTTER, SoundSource.BLOCKS, 0.5f, Random.nextDouble(0.8, 1.2).toFloat())
                    val lensStack = ItemStack(RECIPES[heldItem]!!)
                    if (!playerIn.addItem(lensStack)) {
                        playerIn.drop(lensStack, false)
                    }
                } else {
                    playerIn.sendMessage(TranslatableComponent("message.afraidofthedark.lens_cutter.wrong_item"))
                }
            } else {
                playerIn.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }

        return InteractionResult.SUCCESS
    }

    companion object {
        private val RECIPES = mapOf(
            Blocks.GLASS.asItem() to ModItems.GLASS_LENS,
            Items.DIAMOND to ModItems.DIAMOND_LENS,
            ModItems.MYSTIC_TOPAZ to ModItems.TOPAZ_LENS
        )
    }
}