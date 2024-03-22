package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.bolt.AstralSilverBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.world.level.Level

/**
 * Class representing a solver bolt item
 *
 * @constructor sets up the item's name
 */
class AstralSilverBoltItem : AOTDBoltItem("astral_silver_bolt", Properties(), ModResearches.SILVER_SLAYER) {
    override fun createBolt(world: Level): BoltEntity {
        return AstralSilverBoltEntity(world)
    }
}
