package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntitySpellAltar
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

/**
 * Spell altar block used as the primary block to craft spells
 *
 * @constructor sets the block name and properties
 */
class BlockSpellAltar : AOTDBlockTileEntity("spell_altar", Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 50.0f)) {
    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity {
        return TileEntitySpellAltar()
    }
}