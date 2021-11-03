package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.feature.tree.MangroveTreeFeature
import com.davidm1a2.afraidofthedark.common.world.feature.tree.SacredMangroveTreeFeature

/**
 * A list of features to be registered
 */
object ModFeatures {
    val MANGROVE_TREE = MangroveTreeFeature()
    val SACRED_MANGROVE_TREE = SacredMangroveTreeFeature()

    val FEATURES = arrayOf(
        MANGROVE_TREE,
        SACRED_MANGROVE_TREE
    )
}