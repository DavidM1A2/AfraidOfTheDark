package com.davidm1a2.afraidofthedark.common.registry

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.research.Research
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

open class BoltEntry constructor(
    val item: Item,
    lazyEntityType: Lazy<EntityType<*>>,
    lazyPrerequisiteResearch: Lazy<Research?>
) : ForgeRegistryEntry<BoltEntry>() {
    // Why do we need these lazy initializers? Because forge loads registries in random order, so the RESEARCH and EntityType registries might not be valid yet
    private val entityType: EntityType<*> by lazyEntityType
    val prerequisiteResearch: Research? by lazyPrerequisiteResearch

    fun makeBoltEntity(world: World): BoltEntity {
        return entityType.create(world) as? BoltEntity ?: throw IllegalStateException("Entity type $entityType is not a subclass of BoltEntity")
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
                ForgeRegistries.ITEMS.codec()
                    .fieldOf("item")
                    .forGetter(BoltEntry::item),
                ForgeRegistries.ENTITIES.codec()
                    .lazy()
                    .fieldOf("entity_type")
                    .forGetter { boltEntry -> lazyOf(boltEntry.entityType) },
                ModRegistries.RESEARCH.codec()
                    .lazy()
                    .optionalFieldOf("prerequisite_research")
                    .forGetter { boltEntry -> boltEntry.prerequisiteResearch.toLazyOptional() }
            ).apply(it, it.stable(Function4 { name, item, entityType, prerequisiteResearch ->
                BoltEntry(item, entityType, prerequisiteResearch.getOrNull()).setRegistryName(name)
            }))
        }
    }
}