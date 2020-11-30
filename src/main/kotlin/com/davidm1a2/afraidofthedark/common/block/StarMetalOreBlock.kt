package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.ToolType

/**
 * Class representing star metal ore found in meteors
 *
 * @constructor initializes the block's name and properties
 */
class StarMetalOreBlock : AOTDBlock(
    "star_metal_ore",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
        .lightValue(4)
) {
    override fun getHarvestLevel(state: BlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
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
        // If the player can research star metal let them
        if (playerResearch.canResearch(ModResearches.STAR_METAL)) {
            playerResearch.setResearch(ModResearches.STAR_METAL, true)
            playerResearch.sync(player, true)
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack)
    }
}