package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.VoidBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.level.Level

class VoidBoltItem : AOTDBoltItem("void_bolt", Properties(), ModResearches.AN_UNSETTLING_MATERIAL) {
    override fun createBolt(world: Level): BoltEntity {
        return VoidBoltEntity(world)
    }
}
