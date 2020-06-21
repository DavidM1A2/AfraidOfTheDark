package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockLeaves
import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.util.IItemProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * A base class for any leaves added by the AOTD mod
 *
 * @constructor just requires the name of the block to register as the registry name
 * @param baseName The name to be used by the registry and unlocalized names
 * @param properties The properties of this block
 */
abstract class AOTDBlockLeaves(baseName: String, properties: Properties) : BlockLeaves(properties.apply {
    hardnessAndResistance(0.2f)
    needsRandomTick()
    sound(SoundType.PLANT)
}), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    abstract override fun getItemDropped(state: IBlockState, world: World, blockPos: BlockPos, fortune: Int): IItemProvider
}