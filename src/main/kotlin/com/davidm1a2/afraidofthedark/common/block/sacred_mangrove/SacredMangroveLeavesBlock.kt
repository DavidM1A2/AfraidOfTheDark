package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeavesBlock
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext

/**
 * Sacred mangrove leaves are grown by sacred mangrove trees
 *
 * @constructor just sets the name of the block
 */
class SacredMangroveLeavesBlock : AOTDLeavesBlock("sacred_mangrove_leaves", Properties.create(Material.LEAVES)) {
    override fun getDrops(state: BlockState, lootContext: LootContext.Builder): List<ItemStack> {
        return emptyList()
    }
}