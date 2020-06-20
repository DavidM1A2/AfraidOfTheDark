package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockLog
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor

/**
 * Class representing a mangrove log block
 *
 * @constructor passes on the name
 */
class BlockMangrove : AOTDBlockLog("mangrove", MaterialColor.WOOD, Properties.create(Material.WOOD, MaterialColor.WOOD))
