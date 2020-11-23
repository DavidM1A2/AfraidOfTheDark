package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType

/**
 * Class representing sunstone ore found in meteors
 *
 * @constructor sets the block's properties like name
 */
class SunstoneOreBlock : AOTDBlock(
    "sunstone_ore",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
        .lightValue(1)
) {
    override fun getHarvestLevel(state: BlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
    }

    override fun getDrops(state: BlockState, lootContext: LootContext.Builder): List<ItemStack> {
        return listOf(ItemStack(ModItems.SUNSTONE_FRAGMENT))
    }

    override fun harvestBlock(
        worldIn: World,
        player: PlayerEntity,
        pos: BlockPos,
        state: BlockState,
        te: TileEntity?,
        stack: ItemStack
    ) {
        val playerResearch = player.getResearch()
        // If the player can research igneous let them
        if (playerResearch.canResearch(ModResearches.IGNEOUS)) {
            playerResearch.setResearch(ModResearches.IGNEOUS, true)
            playerResearch.sync(player, true)
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack)
    }
}