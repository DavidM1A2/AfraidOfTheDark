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
 * Class that represents an astral silver ore block
 *
 * @constructor initializes the block's properties
 */
class AstralSilverOreBlock : AOTDBlock(
    "astral_silver_ore",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
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
        // If the player can unlock the astral silver research unlock it and sync
        val playerResearch = player.getResearch()
        if (playerResearch.canResearch(ModResearches.ASTRAL_SILVER)) {
            playerResearch.setResearch(ModResearches.ASTRAL_SILVER, true)
            playerResearch.sync(player, true)
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack)
    }
}