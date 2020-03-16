package com.davidm1a2.afraidofthedark.client.debug

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.Block

/**
 * Utility class used to make model json files. This isn't used by the mod at all
 */
object ItemModelCreator {
    /**
     * Creates model files for all blocks
     */
    fun createModels() {
        ModBlocks.BLOCK_LIST.forEach {
            createModel(it)
        }
    }

    /**
     * Creates a model file for a specific block
     */
    fun createModel(block: Block) {
        when (block) {
            is AOTDBlock -> {
            }
            else -> AfraidOfTheDark.INSTANCE.logger.error("Couldn't create model for ${block.registryName}")
        }
    }
}