package com.davidm1a2.afraidofthedark.common.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import com.mojang.datafixers.util.Function8
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry
import java.util.*

class Research(
    val xPosition: Int,
    val yPosition: Int,
    val researchedRecipes: List<Item>,
    val preResearchedRecipes: List<Item>,
    val icon: ResourceLocation,
    val triggers: List<ConfiguredResearchTrigger<*, *>>,
    preRequisiteId: ResourceLocation?
) : ForgeRegistryEntry<Research>() {
    // Why do we need this lazy initializer? Because forge loads registries in random order, so the RESEARCH registry might not be valid yet
    val preRequisite: Research? by lazy {
        preRequisiteId?.let { ModRegistries.RESEARCH.getValue(it) }
    }

    fun getUnlocalizedName(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.name"
    }

    fun getUnlocalizedTooltip(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.tooltip"
    }

    fun getUnlocalizedPreText(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.pre_text"
    }

    fun getUnlocalizedText(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.text"
    }

    companion object {
        val CODEC: Codec<Research> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.fieldOf("forge:registry_name").forGetter(Research::getRegistryName),
                Codec.INT.fieldOf("x").forGetter(Research::xPosition),
                Codec.INT.fieldOf("y").forGetter(Research::yPosition),
                ResourceLocation.CODEC
                    .xmap({ location -> ForgeRegistries.ITEMS.getValue(location)!! }) { item -> item?.registryName }
                    .listOf()
                    .fieldOf("recipes")
                    .forGetter(Research::researchedRecipes),
                ResourceLocation.CODEC
                    .xmap({ location -> ForgeRegistries.ITEMS.getValue(location)!! }) { item -> item?.registryName }
                    .listOf()
                    .fieldOf("pre_recipes")
                    .forGetter(Research::preResearchedRecipes),
                ResourceLocation.CODEC.fieldOf("icon").forGetter(Research::icon),
                ConfiguredResearchTrigger.CODEC
                    .listOf()
                    .optionalFieldOf("triggers", emptyList())
                    .forGetter(Research::triggers),
                ResourceLocation.CODEC
                    .optionalFieldOf("prerequisite")
                    .forGetter { research -> Optional.ofNullable(research.preRequisite?.registryName) }
            ).apply(it, it.stable(Function8 { name, x, y, recipes, preRecipes, icon, triggers, prerequisite ->
                Research(x, y, recipes, preRecipes, icon, triggers, prerequisite.orElse(null)).setRegistryName(name)
            }))
        }
    }
}