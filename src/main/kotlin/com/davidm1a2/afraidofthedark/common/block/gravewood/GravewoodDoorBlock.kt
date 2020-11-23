package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDDoorBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood door block
 *
 * @constructor just sets the registry name
 */
class GravewoodDoorBlock : AOTDDoorBlock("gravewood_door", Properties.create(Material.WOOD, ModBlocks.GRAVEWOOD.materialColor))