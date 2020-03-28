package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockDoor
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.material.Material
import net.minecraft.item.Item

/**
 * Class representing a mangrove door block
 *
 * @constructor just sets the registry and unlocalized name
 */
class BlockMangroveDoor : AOTDBlockDoor("mangrove_door", Material.WOOD) {
    /**
     * @return The item that places this door
     */
    override fun getItem(): Item {
        return ModItems.MANGROVE_DOOR
    }
}