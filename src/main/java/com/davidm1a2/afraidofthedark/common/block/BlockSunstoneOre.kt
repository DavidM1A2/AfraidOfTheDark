package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

/**
 * Class representing sunstone ore found in meteors
 *
 * @constructor sets the block's properties like name
 */
class BlockSunstoneOre : AOTDBlock("sunstone_ore", Material.ROCK) {
    init {
        setLightLevel(1.0f)
        setHardness(10.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 2)
    }

    /**
     * Gets the item that should be dropped when the block is harvested
     *
     * @param state   The block that was broken
     * @param rand    The random to use if drops should be random
     * @param fortune The fortune level of the tool used to break the block
     * @return The item that should be dropped upon breaking the block
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return ModItems.SUNSTONE_FRAGMENT
    }

    /**
     * Called when the block is broken, here we check if the player can unlock the igneous research, if so unlock it
     *
     * @param worldIn The world that the block was broken in
     * @param player The player that broke the block
     * @param pos The position that the block was broken at
     * @param state The state of the block before being broken
     * @param te The tile entity inside the broken block
     * @param stack The item that was created as a result of breaking the block
     */
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