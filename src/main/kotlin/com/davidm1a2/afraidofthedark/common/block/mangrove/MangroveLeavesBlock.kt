package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeavesBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext

/**
 * Mangrove leaves are grown by mangrove trees
 *
 * @constructor just sets the name of the block
 */
class MangroveLeavesBlock : AOTDLeavesBlock("mangrove_leaves", Properties.create(Material.LEAVES)) {
    override fun getDrops(state: BlockState, lootContext: LootContext.Builder): List<ItemStack> {
        return listOf(ItemStack(ModBlocks.MANGROVE_SAPLING))
    }
}