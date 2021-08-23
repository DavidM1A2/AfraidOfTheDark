package com.davidm1a2.afraidofthedark.common.registry

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry
import java.util.*

open class BoltEntry constructor(
    val item: Item,
    entityTypeId: ResourceLocation,
    prerequisiteResearchId: ResourceLocation?
) : ForgeRegistryEntry<BoltEntry>() {
    // Why do we need these lazy initializers? Because forge loads registries in random order, so the RESEARCH and EntityType registries might not be valid yet
    private val entityType: EntityType<*> by lazy {
        EntityType.byString(entityTypeId.toString()).orElseThrow {
            IllegalStateException("Entity type $entityTypeId for bolt entry could not be found.")
        }
    }
    val boltEntityFactory: (World) -> BoltEntity by lazy {
        { world -> (entityType.create(world) as? BoltEntity) ?: throw IllegalStateException("Entity type $entityTypeId is not a subclass of BoltEntity") }
    }
    val prerequisiteResearch: Research? by lazy {
        prerequisiteResearchId?.let { ModRegistries.RESEARCH.getValue(it) }
    }

    /**
     * @return The unlocalized name of the bolt entry
     */
    fun getUnlocalizedName(): String {
        return "bolt_entry.${registryName!!.namespace}.${registryName!!.path}"
    }

    companion object {
        val CODEC: Codec<BoltEntry> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.fieldOf("forge:registry_name").forGetter(BoltEntry::getRegistryName),
                ResourceLocation.CODEC
                    .xmap({ location -> ForgeRegistries.ITEMS.getValue(location)!! }) { item -> item?.registryName }
                    .fieldOf("item")
                    .forGetter(BoltEntry::item),
                ResourceLocation.CODEC
                    .fieldOf("entity_type")
                    .forGetter { boltEntry -> boltEntry.entityType.registryName },
                ResourceLocation.CODEC
                    .optionalFieldOf("prerequisite_research")
                    .forGetter { boltEntry -> Optional.ofNullable(boltEntry.prerequisiteResearch?.registryName) }
            ).apply(it, it.stable(Function4 { name, item, entityType, prerequisiteResearch ->
                BoltEntry(item, entityType, prerequisiteResearch.orElse(null)).setRegistryName(name)
            }))
        }
    }
}