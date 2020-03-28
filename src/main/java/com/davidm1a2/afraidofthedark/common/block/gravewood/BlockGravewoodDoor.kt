package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockDoor
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.material.Material
import net.minecraft.item.Item

/**
 * Class representing a gravewood door block
 *
 * @constructor just sets the registry and unlocalized name
 */
class BlockGravewoodDoor : AOTDBlockDoor("gravewood_door", Material.WOOD) {
    /**
     * @return The item that places this door
     */
    override fun getItem(): Item {
        return ModItems.GRAVEWOOD_DOOR
    }
}