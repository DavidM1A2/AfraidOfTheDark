package com.davidm1a2.afraidofthedark.common.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.getOrNull
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.registry.toLazyOptional
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import com.mojang.datafixers.util.Function6
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

class Research(
    val researchedRecipes: List<Item>,
    val preResearchedRecipes: List<Item>,
    val icon: ResourceLocation,
    lazyTriggers: Lazy<List<ConfiguredResearchTrigger<*, *, *>>>,
    lazyPrerequisite: Lazy<Research?>
) : ForgeRegistryEntry<Research>() {
    var xPosition: Double = 0.0
    var yPosition: Double = 0.0

    // Why do we need this lazy initializer? Because forge loads registries in random order, so the RESEARCH registry might not be valid yet
    val triggers: List<ConfiguredResearchTrigger<*, *, *>> by lazyTriggers
    val preRequisite: Research? by lazyPrerequisite

    fun getName(): ITextComponent {
        return TranslationTextComponent("research.${registryName!!.namespace}.${registryName!!.path}.name")
    }

    fun getTooltip(): ITextComponent {
        return TranslationTextComponent("research.${registryName!!.namespace}.${registryName!!.path}.tooltip")
    }

    fun getPreText(): ITextComponent {
        return TranslationTextComponent("research.${registryName!!.namespace}.${registryName!!.path}.pre_text")
    }

    fun getText(): ITextComponent {
        return TranslationTextComponent("research.${registryName!!.namespace}.${registryName!!.path}.text")
    }

    companion object {
        val CODEC: Codec<Research> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.fieldOf("forge:registry_name").forGetter(Research::getRegistryName),
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
            ).apply(it, it.stable(Function6 { name, recipes, preRecipes, icon, triggers, prerequisite ->
                Research(recipes, preRecipes, icon, triggers, prerequisite.getOrNull()).setRegistryName(name)
            }))
        }
    }
}