package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.grower.AbstractTreeGrower

/**
 * Class representing the base for a sapling created in afraid of the dark
 *
 * @constructor sets up default sapling properties
 * @param baseName The name of the block to register
 * @param tree The tree that grows for the sapling
 * @param properties The properties of the block
 */
abstract class AOTDSaplingBlock(baseName: String, tree: AbstractTreeGrower, properties: Properties) : SaplingBlock(tree, properties.apply {
    noCollission()
    randomTicks()
    instabreak()
    sound(SoundType.GRASS)
}), IShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}