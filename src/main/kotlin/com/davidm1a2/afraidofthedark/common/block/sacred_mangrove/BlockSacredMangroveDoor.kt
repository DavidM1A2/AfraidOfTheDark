package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockDoor
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a sacred mangrove door block
 *
 * @constructor just sets the registry and unlocalized name
 */
class BlockSacredMangroveDoor : AOTDBlockDoor("sacred_mangrove_door", Properties.create(Material.WOOD, ModBlocks.SACRED_MANGROVE.getRawMaterialColor()))