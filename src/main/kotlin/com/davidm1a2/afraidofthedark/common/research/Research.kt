package com.davidm1a2.afraidofthedark.common.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.getOrNull
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.registry.toLazyOptional
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import com.mojang.datafixers.util.Function10
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

class Research(
    lazyRecipes: Lazy<List<Item>>,
    lazyPreRecipes: Lazy<List<Item>>,
    val icon: ResourceLocation,
    lazyTriggers: Lazy<List<ConfiguredResearchTrigger<*, *, *>>>,
    lazyPrerequisite: Lazy<Research?>,
    val stickers: List<ResourceLocation>,
    val preStickers: List<ResourceLocation>,
    val spellComponents: ResearchSpellComponents,
    val preSpellComponents: ResearchSpellComponents
) : ForgeRegistryEntry<Research>() {
    var xPosition: Double = 0.0
    var yPosition: Double = 0.0

    // Why do we need this lazy initializer? Because forge loads registries in random order, so the RESEARCH registry might not be valid yet
    val triggers: List<ConfiguredResearchTrigger<*, *, *>> by lazyTriggers
    val preRequisite: Research? by lazyPrerequisite
    val recipes: List<Item> by lazyRecipes
    val preRecipes: List<Item> by lazyPreRecipes

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
                    .lazy()
                    .fieldOf("recipes")
                    .forGetter { research -> lazyOf(research.recipes) },
                ForgeRegistries.ITEMS.codec()
                    .listOf()
                    .lazy()
                    .fieldOf("pre_recipes")
                    .forGetter { research -> lazyOf(research.preRecipes) },
                ResourceLocation.CODEC.fieldOf("icon").forGetter(Research::icon),
                ConfiguredResearchTrigger.CODEC
                    .listOf()
                    .lazy()
                    .fieldOf("triggers")
                    .forGetter { research -> lazyOf(research.triggers) },
                ModRegistries.RESEARCH.codec()
                    .lazy()
                    .optionalFieldOf("prerequisite")
                    .forGetter { research -> research.preRequisite.toLazyOptional() },
                ResourceLocation.CODEC
                    .listOf()
                    .optionalFieldOf("stickers", emptyList())
                    .forGetter { research -> research.stickers },
                ResourceLocation.CODEC
                    .listOf()
                    .optionalFieldOf("pre_stickers", emptyList())
                    .forGetter { research -> research.preStickers },
                ResearchSpellComponents.CODEC
                    .optionalFieldOf("spell_components", ResearchSpellComponents.EMPTY)
                    .forGetter { research -> research.spellComponents },
                ResearchSpellComponents.CODEC
                    .optionalFieldOf("pre_spell_components", ResearchSpellComponents.EMPTY)
                    .forGetter { research -> research.preSpellComponents }
            ).apply(it, it.stable(Function10 { name, recipes, preRecipes, icon, triggers, prerequisite, stickers, preStickers, spellComponents, preSpellComponents ->
                Research(recipes, preRecipes, icon, triggers, prerequisite.getOrNull(), stickers, preStickers, spellComponents, preSpellComponents).setRegistryName(name)
            }))
        }
    }
}