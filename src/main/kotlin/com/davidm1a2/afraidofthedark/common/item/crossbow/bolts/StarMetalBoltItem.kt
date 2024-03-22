package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.StarMetalBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.level.Level

/**
 * Class representing a star metal bolt item
 *
 * @constructor sets up the item's name
 */
class StarMetalBoltItem : AOTDBoltItem("star_metal_bolt", Properties(), ModResearches.STAR_METAL) {
    override fun createBolt(world: Level): BoltEntity {
        return StarMetalBoltEntity(world)
    }
}
