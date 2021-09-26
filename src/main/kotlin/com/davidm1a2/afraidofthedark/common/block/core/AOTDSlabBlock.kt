package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.SlabBlock

/**
 * Base class for all AOTD slabs
 *
 * @constructor sets the name of the slab and the material
 * @param baseName The base name of the slab
 * @param properties The properties of the block
 */
abstract class AOTDSlabBlock(baseName: String, properties: Properties) : SlabBlock(properties), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}