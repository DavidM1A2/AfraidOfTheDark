package com.davidm1a2.afraidofthedark.common.registry.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityBolt
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

/**
 * Base class for all AOtD bolt entries
 *
 * @constructor sets the class fields
 * @param name              The name this bolt entry will have in the registry
 * @param boltItem          The item that this bolt entry represents
 * @param boltEntityFactory The entity that this bolt entry represents
 * @param preRequisite      The research that is required to use this bolt
 */
class AOTDBoltEntry(
    name: String,
    boltItem: Item,
    boltEntityFactory: (World, EntityPlayer) -> EntityBolt,
    preRequisite: Research?
) :
    BoltEntry(boltItem, boltEntityFactory, preRequisite) {
    init {
        registryName = ResourceLocation(Constants.MOD_ID, name)
    }
}