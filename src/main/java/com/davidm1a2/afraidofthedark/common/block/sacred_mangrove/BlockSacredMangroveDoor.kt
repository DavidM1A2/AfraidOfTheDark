package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDDoor
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.material.Material
import net.minecraft.item.Item

/**
 * Class representing a sacred mangrove door block
 *
 * @constructor just sets the registry and unlocalized name
 */
class BlockSacredMangroveDoor : AOTDDoor("sacred_mangrove_door", Material.WOOD)
{
    /**
     * @return The item that places this door
     */
    override fun getItem(): Item
    {
        return ModItems.SACRED_MANGROVE_DOOR
    }
}