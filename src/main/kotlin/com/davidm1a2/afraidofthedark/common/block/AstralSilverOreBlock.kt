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
 * Class that represents an astral silver ore block
 *
 * @constructor initializes the block's properties
 */
class AstralSilverOreBlock : AOTDBlock(
    "astral_silver_ore",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
) {
    override fun removedByPlayer(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, willHarvest: Boolean, fluid: FluidState): Boolean {
        if (willHarvest) {
            // If the player can unlock the astral silver research unlock it and sync
            val playerResearch = player.getResearch()
            if (playerResearch.canResearch(ModResearches.ASTRAL_SILVER)) {
                playerResearch.setResearch(ModResearches.ASTRAL_SILVER, true)
                playerResearch.sync(player, true)
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid)
    }
}