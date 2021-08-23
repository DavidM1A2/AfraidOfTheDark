package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.mojang.datafixers.util.Function7
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
    preRequisiteId: ResourceLocation?
) : ForgeRegistryEntry<Research>() {
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
                    .fieldOf("preRecipes")
                    .forGetter(Research::preResearchedRecipes),
                ResourceLocation.CODEC.fieldOf("icon").forGetter(Research::icon),
                ResourceLocation.CODEC
                    .optionalFieldOf("prerequisite")
                    .forGetter { research -> Optional.ofNullable(research.preRequisite?.registryName) }
            ).apply(it, it.stable(Function7 { name, x, y, recipes, preRecipes, icon, prerequisite ->
                Research(x, y, recipes, preRecipes, icon, prerequisite.orElse(null)).setRegistryName(name)
            }))
        }
    }
}