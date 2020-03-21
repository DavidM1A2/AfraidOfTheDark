package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemDoor

/**
 * Item that spawns a gravewood door upon right clicking. This is required since doors are "2 block structures"
 */
class ItemGravewoodDoor : AOTDItemDoor(ModBlocks.GRAVEWOOD_DOOR)