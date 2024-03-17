package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLogBlock
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor

/**
 * Class representing a gravewood log block
 *
 * @constructor just sets the registry and unlocalized name
 */
class GravewoodBlock : AOTDLogBlock("gravewood", Properties.of(Material.WOOD, MaterialColor.WOOD))
