package com.davidm1a2.afraidofthedark.common.registry.bolt

import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityBolt
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.world.World
import net.minecraftforge.registries.IForgeRegistryEntry

/**
 * Base class for all bolt entries, bolt entries are used to define new bolt item/entity/research/name types
 *
 * @constructor sets the class fields
 * @param boltItem The item that this bolt entry represents
 * @param boltEntityFactory The entity factory that creates this bolt once shot
 * @param preRequisite The pre-requisite research that needs to be researched for this to be used
 */
abstract class BoltEntry constructor(
        val boltItem: Item,
        val boltEntityFactory: (World, EntityPlayer) -> EntityBolt,
        val preRequisite: Research?
) : IForgeRegistryEntry.Impl<BoltEntry>()
{
    /**
     * @return The unlocalized name of the bolt entry
     */
    fun getUnlocalizedName(): String
    {
        return "bolt_entry.${registryName!!.resourceDomain}:${registryName!!.resourcePath}"
    }
}