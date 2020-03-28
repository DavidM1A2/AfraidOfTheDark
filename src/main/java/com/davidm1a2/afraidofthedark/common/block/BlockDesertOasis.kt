package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityDesertOasis
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.world.World
import java.util.*

/**
 * Desert oasis block used to monitor the state of the desert oasis dungeon
 *
 * @constructor sets the block name and properties
 */
class BlockDesertOasis : AOTDBlockTileEntity("desert_oasis", Material.ROCK, false) {
    init {
        // Set the block name and material
        // Make this block hard to destroy with explosives and only harvestable with a diamond pick
        setHardness(10.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 3)
    }

    /**
     * This tile entity just renders as a normal block
     *
     * @param state The block state to render
     * @return MODEL, since it's not an animated tile entity
     */
    override fun getRenderType(state: IBlockState): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }

    /**
     * Creates a tile entity that monitors for nearby players
     *
     * @param worldIn The world the block is in
     * @param meta    The block's metadata value
     * @return The dark forest tile entity instance
     */
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileEntityDesertOasis()
    }

    /**
     * The dark forest block cannot be dropped and picked up
     */
    override fun quantityDropped(random: Random): Int {
        return 0
    }
}