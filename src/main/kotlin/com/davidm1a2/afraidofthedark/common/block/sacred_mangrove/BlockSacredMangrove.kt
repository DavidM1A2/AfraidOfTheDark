package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockLog
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor

/**
 * Class representing a sacred mangrove log block
 *
 * @constructor passes on the name
 */
class BlockSacredMangrove : AOTDBlockLog("sacred_mangrove", MaterialColor.WOOD, Properties.create(Material.WOOD, MaterialColor.WOOD))