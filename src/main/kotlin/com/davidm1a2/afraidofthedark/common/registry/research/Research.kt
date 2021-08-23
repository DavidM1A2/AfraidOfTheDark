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
        /*
        TODO: Add triggers field

        private fun procTrigger(trigger: JsonObject, research: Research) {
            when (JSONUtils.getAsString(trigger, "type")) {
                "takeDamage" -> {
                    val fromEntity = JSONUtils.getAsString(trigger, "entity")
                    AfraidOfTheDark.researchHooks.addHook(LivingDamageEvent::class) {
                        val event = it as LivingDamageEvent
                        if (event.entity is PlayerEntity) {
                            val player = event.entity as PlayerEntity
                            val playerResearch = player.getResearch()
                            if (playerResearch.canResearch(research)) {
                                if (event.source.entity != null) {
                                    if (event.source.entity!!.type.registryName.toString() == fromEntity) {
                                        if (!JSONUtils.getAsBoolean(trigger, "mustSurvive") || player.health > event.amount) {
                                            playerResearch.setResearch(research, true)
                                            playerResearch.sync(player, true)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
         */
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
                ResourceLocation.CODEC
                    .optionalFieldOf("prerequisite")
                    .forGetter { research -> Optional.ofNullable(research.preRequisite?.registryName) }
            ).apply(it, it.stable(Function7 { name, x, y, recipes, preRecipes, icon, prerequisite ->
                Research(x, y, recipes, preRecipes, icon, prerequisite.orElse(null)).setRegistryName(name)
            }))
        }
    }
}