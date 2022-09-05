package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ActionResultType
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
    Properties.of(Material.METAL)
        .strength(5.0F, 6.0F)
        .sound(SoundType.METAL)
) {
    override fun use(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        result: BlockRayTraceResult
    ): ActionResultType {
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
                    worldIn.playSound(null, pos, ModSounds.LENS_CUTTER, SoundCategory.BLOCKS, 0.5f, Random.nextDouble(0.8, 1.2).toFloat())
                    val lensStack = ItemStack(RECIPES[heldItem]!!)
                    if (!playerIn.addItem(lensStack)) {
                        playerIn.drop(lensStack, false)
                    }
                } else {
                    playerIn.sendMessage(TranslationTextComponent("message.afraidofthedark.lens_cutter.wrong_item"))
                }
            } else {
                playerIn.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }

        return ActionResultType.SUCCESS
    }

    companion object {
        private val RECIPES = mapOf(
            Blocks.GLASS.asItem() to ModItems.GLASS_LENS,
            Items.DIAMOND to ModItems.DIAMOND_LENS,
            ModItems.MYSTIC_TOPAZ to ModItems.TOPAZ_LENS
        )
    }
}