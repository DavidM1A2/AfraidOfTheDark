package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IItemProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.ToolType

/**
 * Class representing sunstone ore found in meteors
 *
 * @constructor sets the block's properties like name
 */
class BlockSunstoneOre : AOTDBlock(
    "sunstone_ore",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
        .lightValue(1)
) {
    override fun getHarvestLevel(state: IBlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }

    override fun getItemDropped(state: IBlockState, world: World, blockPos: BlockPos, fortune: Int): IItemProvider {
        return ModItems.SUNSTONE_FRAGMENT
    }

    override fun harvestBlock(
        worldIn: World,
        player: EntityPlayer,
        pos: BlockPos,
        state: IBlockState,
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