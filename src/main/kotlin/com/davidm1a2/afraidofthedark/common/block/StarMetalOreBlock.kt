package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
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
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .lightLevel { 4 }
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
) {
    override fun removedByPlayer(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, willHarvest: Boolean, fluid: FluidState): Boolean {
        if (willHarvest) {
            val playerResearch = player.getResearch()
            // If the player can research star metal let them
            if (playerResearch.canResearch(ModResearches.STAR_METAL)) {
                playerResearch.setResearch(ModResearches.STAR_METAL, true)
                playerResearch.sync(player, true)
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid)
    }
}