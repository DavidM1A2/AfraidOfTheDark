package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.EldritchMetalBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.level.Level

class EldritchMetalBoltItem : AOTDBoltItem("eldritch_metal_bolt", Properties(), ModResearches.AN_UNSETTLING_MATERIAL) {
    override fun createBolt(world: Level): BoltEntity {
        return EldritchMetalBoltEntity(world)
    }
}
