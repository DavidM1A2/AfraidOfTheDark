package com.davidm1a2.afraidofthedark.common.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.getOrNull
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.registry.toLazyOptional
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import com.mojang.datafixers.util.Function8
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

class Research(
    var xPosition: Double,
    var yPosition: Double,
    val researchedRecipes: List<Item>,
    val preResearchedRecipes: List<Item>,
    val icon: ResourceLocation,
    lazyTriggers: Lazy<List<ConfiguredResearchTrigger<*, *, *>>>,
    lazyPrerequisite: Lazy<Research?>
) : ForgeRegistryEntry<Research>() {
    // Why do we need this lazy initializer? Because forge loads registries in random order, so the RESEARCH registry might not be valid yet
    val triggers: List<ConfiguredResearchTrigger<*, *, *>> by lazyTriggers
    val preRequisite: Research? by lazyPrerequisite

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
                Codec.DOUBLE.fieldOf("x").forGetter(Research::xPosition),
                Codec.DOUBLE.fieldOf("y").forGetter(Research::yPosition),
                ForgeRegistries.ITEMS.codec()
                    .listOf()
                    .fieldOf("recipes")
                    .forGetter(Research::researchedRecipes),
                ForgeRegistries.ITEMS.codec()
                    .listOf()
                    .fieldOf("pre_recipes")
                    .forGetter(Research::preResearchedRecipes),
                ResourceLocation.CODEC.fieldOf("icon").forGetter(Research::icon),
                ConfiguredResearchTrigger.CODEC
                    .listOf()
                    .lazy()
                    .fieldOf("triggers")
                    .forGetter { research -> lazyOf(research.triggers) },
                ModRegistries.RESEARCH.codec()
                    .lazy()
                    .optionalFieldOf("prerequisite")
                    .forGetter { research -> research.preRequisite.toLazyOptional() }
            ).apply(it, it.stable(Function8 { name, x, y, recipes, preRecipes, icon, triggers, prerequisite ->
                Research(x, y, recipes, preRecipes, icon, triggers, prerequisite.getOrNull()).setRegistryName(name)
            }))
        }
    }
}