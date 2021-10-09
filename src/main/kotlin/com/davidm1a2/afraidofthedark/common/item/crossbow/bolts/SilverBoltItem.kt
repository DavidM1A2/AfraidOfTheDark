package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.SilverBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.World

/**
 * Class representing a solver bolt item
 *
 * @constructor sets up the item's name
 */
class SilverBoltItem : AOTDBoltItem("silver_bolt", Properties(), ModResearches.ASTRAL_SILVER) {
    override fun createBolt(world: World): BoltEntity {
        return SilverBoltEntity(world)
    }
}
