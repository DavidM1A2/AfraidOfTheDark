package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry

open class Research(
    val xPosition: Int,
    val zPosition: Int,
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
}