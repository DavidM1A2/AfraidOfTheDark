package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockSapling
import net.minecraft.block.SoundType
import net.minecraft.block.trees.AbstractTree

/**
 * Class representing the base for a sapling created in afraid of the dark
 *
 * @constructor sets up default sapling properties
 * @param baseName The name of the block to register
 * @param tree The tree that grows for the sapling
 * @param properties The properties of the block
 */
abstract class AOTDBlockSapling(baseName: String, tree: AbstractTree, properties: Properties) : BlockSapling(tree, properties.apply {
    doesNotBlockMovement()
    needsRandomTick()
    hardnessAndResistance(0f)
    sound(SoundType.PLANT)
}), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}