package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.AmorphousMetalBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.level.Level

class AmorphousMetalBoltItem : AOTDBoltItem("amorphous_metal_bolt", Properties(), ModResearches.CATALYSIS) {
    override fun createBolt(world: Level): BoltEntity {
        return AmorphousMetalBoltEntity(world)
    }
}
