package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.SaplingBlock
import net.minecraft.block.SoundType
import net.minecraft.block.trees.Tree

/**
 * Class representing the base for a sapling created in afraid of the dark
 *
 * @constructor sets up default sapling properties
 * @param baseName The name of the block to register
 * @param tree The tree that grows for the sapling
 * @param properties The properties of the block
 */
abstract class AOTDSaplingBlock(baseName: String, tree: Tree, properties: Properties) :
    SaplingBlock(
        tree,
        properties.apply {
            noCollission()
            randomTicks()
            instabreak()
            sound(SoundType.GRASS)
        }
    ),
    AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}
