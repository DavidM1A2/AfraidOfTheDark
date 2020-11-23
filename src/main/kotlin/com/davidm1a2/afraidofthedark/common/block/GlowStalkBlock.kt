package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext

/**
 * Class representing the glow stalk mushroom stem
 *
 * @constructor sets the item name and makes it glow
 */
class GlowStalkBlock : AOTDBlock(
    "glow_stalk",
    Properties.create(Material.EARTH)
        .lightValue(1)
        .hardnessAndResistance(1.0f, 4.0f)
) {
    override fun getDrops(p_220076_1_: BlockState, p_220076_2_: LootContext.Builder): List<ItemStack> {
        val count = kotlin.random.Random.nextInt(10)
        return List(count) {
            ItemStack(if (kotlin.random.Random.nextBoolean()) Blocks.BROWN_MUSHROOM else Blocks.RED_MUSHROOM)
        }
    }
}