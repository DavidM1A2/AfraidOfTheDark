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
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry
import java.util.Optional

class Research(
    lazyResearchedRecipes: Lazy<List<Item>>,
    lazyPreResearchedRecipes: Lazy<List<Item>>,
    val icon: ResourceLocation,
    lazyTriggers: Lazy<List<ConfiguredResearchTrigger<*, *, *>>>,
    lazyPrerequisite: Lazy<Research?>,
    val stickers: List<ResourceLocation>,
    val preStickers: List<ResourceLocation>
) : ForgeRegistryEntry<Research>() {
    var xPosition: Double = 0.0
    var yPosition: Double = 0.0

    // Why do we need this lazy initializer? Because forge loads registries in random order, so the RESEARCH registry might not be valid yet
    val triggers: List<ConfiguredResearchTrigger<*, *, *>> by lazyTriggers
    val preRequisite: Research? by lazyPrerequisite
    val researchedRecipes: List<Item> by lazyResearchedRecipes
    val preResearchedRecipes: List<Item> by lazyPreResearchedRecipes

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
                    .forGetter { research -> lazyOf(research.researchedRecipes) },
                ForgeRegistries.ITEMS.codec()
                    .listOf()
                    .lazy()
                    .fieldOf("pre_recipes")
                    .forGetter { research -> lazyOf(research.preResearchedRecipes) },
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
                    .optionalFieldOf("stickers")
                    .forGetter { research -> Optional.of(research.stickers) },
                ResourceLocation.CODEC
                    .listOf()
                    .optionalFieldOf("pre_stickers")
                    .forGetter { research -> Optional.of(research.preStickers) }
            ).apply(it, it.stable(Function8 { name, recipes, preRecipes, icon, triggers, prerequisite, stickers, preStickers ->
                Research(recipes, preRecipes, icon, triggers, prerequisite.getOrNull(), stickers.orElse(emptyList()), preStickers.orElse(emptyList())).setRegistryName(name)
            }))
        }
    }
}