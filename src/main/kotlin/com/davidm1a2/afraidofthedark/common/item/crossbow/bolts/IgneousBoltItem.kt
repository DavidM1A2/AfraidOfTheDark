package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.IgneousBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.World

/**
 * Class representing an igneous bolt item
 *
 * @constructor sets up the item's name
 */
class IgneousBoltItem : AOTDBoltItem("igneous_bolt", Properties(), ModResearches.IGNEOUS) {
    override fun createBolt(world: World): BoltEntity {
        return IgneousBoltEntity(world)
    }
}
