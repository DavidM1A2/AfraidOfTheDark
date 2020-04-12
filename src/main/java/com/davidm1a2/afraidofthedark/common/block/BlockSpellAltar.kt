package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntitySpellAltar
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 * Spell altar block used as the primary block to craft spells
 *
 * @constructor sets the block name and properties
 */
class BlockSpellAltar : AOTDBlockTileEntity("spell_altar", Material.ROCK) {
    init {
        // Make this block hard to destroy with explosives
        setHardness(2.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 2)
    }

    /**
     * False, it has a special renderer
     *
     * @param state The state to check
     * @return False, this is a tile entity with a special renderer
     */
    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    /**
     * Creates a tile entity that monitors for nearby players
     *
     * @param worldIn The world the block is in
     * @param meta    The block's metadata value
     * @return The dark forest tile entity instance
     */
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileEntitySpellAltar()
    }
}