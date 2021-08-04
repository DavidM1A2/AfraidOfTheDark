package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDDoorBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove door block
 *
 * @constructor just sets the registry name
 */
class MangroveDoorBlock : AOTDDoorBlock("mangrove_door", Properties.of(Material.WOOD, ModBlocks.MANGROVE.defaultMaterialColor()))
