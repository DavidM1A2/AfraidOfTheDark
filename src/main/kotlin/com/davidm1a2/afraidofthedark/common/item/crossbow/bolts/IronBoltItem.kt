package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.IronBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.World

/**
 * Class representing an iron bolt item
 *
 * @constructor sets up the item's name
 */
class IronBoltItem : AOTDBoltItem("iron_bolt", Properties()) {
    override fun createBolt(world: World): BoltEntity {
        return IronBoltEntity(world)
    }
}
