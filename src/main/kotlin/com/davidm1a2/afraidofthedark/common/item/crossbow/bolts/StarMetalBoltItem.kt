package com.davidm1a2.afraidofthedark.common.item.crossbow.bolts

import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.StarMetalBoltEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * Class representing a star metal bolt item
 *
 * @constructor sets up the item's name
 */
class StarMetalBoltItem : AOTDBoltItem("star_metal_bolt", Properties()) {
    override fun createBolt(world: World, itemStack: ItemStack, shooter: LivingEntity): BoltEntity {
        return StarMetalBoltEntity(world)
    }
}
