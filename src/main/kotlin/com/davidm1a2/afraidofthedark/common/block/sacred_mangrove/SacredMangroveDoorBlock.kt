package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDDoorBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.world.level.material.Material

/**
 * Class representing a sacred mangrove door block
 *
 * @constructor just sets the registry and unlocalized name
 */
class SacredMangroveDoorBlock : AOTDDoorBlock("sacred_mangrove_door", Properties.of(Material.WOOD, ModBlocks.SACRED_MANGROVE.defaultMaterialColor()))