package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.WoodenBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.World

/**
 * Class representing a wooden bolt item
 *
 * @constructor sets up the item's name
 */
class WoodenBoltItem : AOTDBoltItem("wooden_bolt", Properties()) {
    override fun createBolt(world: World): BoltEntity {
        return WoodenBoltEntity(world)
    }
}
